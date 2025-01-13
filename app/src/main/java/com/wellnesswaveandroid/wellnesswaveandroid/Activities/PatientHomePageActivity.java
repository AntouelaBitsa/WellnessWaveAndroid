package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.AppointmentsApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.CustomZoomLayoutManager;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.PatAppointmentCarouselAdapter;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.PopUpRecyclerAdapter;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.SpecializationCarouselAdapter;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.SpecializationSet;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientHomePageActivity extends AppCompatActivity {

    //Carousel of appointments elements
    private RecyclerView recyclerView;
    private PatAppointmentCarouselAdapter appointmentCarouselAdapter;
    private ArrayList<Appointments> appointmentsList;
    private Integer patientID;
    private TextView patUsername, docFNamePopUp, docLNamePopUp, docSpecPopUp, docPhoneNumPopUp, docAddressPopUp, docCommPopUp;
    private String pUsername;
    private Button rescheduleBtnPopUp;
    private Dialog appointDetPopUp, specializationPopUp;

    //Carousel of specializations elements
    private RecyclerView specializationRecyclerView, specPopUpRecycler;
    private SpecializationCarouselAdapter specializationCarouselAdapter;
    private List<SpecializationSet> specialization;
    private BottomNavigationView bottomNavigationView;
    private PopUpRecyclerAdapter popUpRecyclerAdapter;
    private Doctor docInstance;

    private List<String> docFullName = new ArrayList<>();
    private ArrayList<Doctor> doctorsBySpecList = new ArrayList<>();
    private Integer selectedDocId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home_page);

        bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    return true;
                }else if (item.getItemId() == R.id.nav_manage_appointments) {
                    //TODO: Go to Manage Appointments (With Patient Data)
                    startActivity(new Intent(getApplicationContext(), BookAppointmentActivity.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.nav_diagn_history) {
                    startActivity(new Intent(getApplicationContext(), DiagnosisRecordsActivity.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.nav_profile) {
                    startActivity(new Intent(getApplicationContext(), PatDetails.class));
                    finish();
                    return true;
                } else {
                    return false;
                }
            }
        });

        //Carousel for Appointments
        patUsername = findViewById(R.id.usernamePatTxt);
        recyclerView = findViewById(R.id.appointmentRecycler);

        //Getting the patient instance ID: singleton pattern
        Patient pat = Patient.getInstance();
        if (pat != null) {
            System.out.println("[-SOS-]: pat Id inside if: " + pat.getPatientId());
            patientID = pat.getPatientId();
            pUsername = pat.getPatUsername();
        }
        System.out.println("[-**0-PatientHomePageActivity] onCreate: patientID= " + patientID);

        /**
         * Carousel Initialization of Patient's upcoming Appointments
         */
        appointmentsList = new ArrayList<>();
        appointmentCarouselAdapter = new PatAppointmentCarouselAdapter(appointmentsList, this,
                new PatAppointmentCarouselAdapter.OnInfoBtnListener() {
            @Override
            public void onInfoButtonClick(Appointments appoint) {
                callAppointDetailsPopUp(appoint);
            }
        });
        recyclerView.setAdapter(appointmentCarouselAdapter);

        if (appointmentsList.isEmpty()){
            Log.d("RecyclerView", "LinearLayoutManager applied.");
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            Log.d("RecyclerView", "Data set updated. Item count: " + appointmentsList.size());
            appointmentCarouselAdapter.notifyDataSetChanged();
        }else {
            Log.d("RecyclerView", "CustomZoomLayoutManager applied.");
            recyclerView.setLayoutManager(new CustomZoomLayoutManager(this));
            appointmentCarouselAdapter.notifyDataSetChanged();
        }

        //GET Request Implementation : getting a list of appointments based on the id of the user
        getAppointmentsInfo(patientID);

        //List of Specialization Icon + Title
        specialization = getSetsOfSpecTitleAndIcon();

        /**
         * Carousel Initialization of specializations of all doctors -> services application is offering
         */
        specializationRecyclerView = findViewById(R.id.docSpecializationRecyclerView);
        specializationRecyclerView.setLayoutManager(new CustomZoomLayoutManager(this));
        SnapHelper snapSpecializationHelper = new LinearSnapHelper();
        snapSpecializationHelper.attachToRecyclerView(recyclerView);

        specializationCarouselAdapter = new SpecializationCarouselAdapter(specialization, this,
                new SpecializationCarouselAdapter.CardOnClickListener() {
                    @Override
                    public void cardOnClickListener(String specializationType) {
                        System.out.println("Specialization Click String from Adapter: " + specializationType);
                        callSpecializationPopUp(specializationType);
                    }
                });
        specializationRecyclerView.setAdapter(specializationCarouselAdapter);
    }

    private void callSpecializationPopUp(String specType) {
        System.out.println("[Debug message] inside callAppointDetailsPopUp(): ");
//        System.out.println("[Debug message] callAppointDetailsPopUp() appointment:" + appointments.toString());

        //Cancel Pop Up Dialog
        specializationPopUp = new Dialog(PatientHomePageActivity.this);
        specializationPopUp.setContentView(R.layout.doc_spcialization_popup_dialog);
        specializationPopUp.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        specializationPopUp.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_popup_background));
        specializationPopUp.setCancelable(true); //when user clicks outside the dialog can not close

        //Pop Up Views
        //DONE: find views by id for text views
        specPopUpRecycler = specializationPopUp.findViewById(R.id.specRecyclerPopUp);
//        specPopUpRecycler.setLayoutManager(new CustomZoomLayoutManager(this));
        specPopUpRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SnapHelper snapSpecializationHelper = new LinearSnapHelper();
        snapSpecializationHelper.attachToRecyclerView(specPopUpRecycler);
        getDoctorsBySpecialization(specType);

//        popUpRecyclerAdapter = new PopUpRecyclerAdapter(doctorsBySpecList, this);
//        specPopUpRecycler.setAdapter(popUpRecyclerAdapter);
//        popUpRecyclerAdapter.notifyDataSetChanged();
        // Show the dialog
        specializationPopUp.show();
    }

    //REQUEST
    private void getDoctorsBySpecialization(String specialization) {
        System.out.println("Specialisation = " + specialization);
        System.out.println("OK1.1");

        RetrofitService retrofitService = new RetrofitService();
        AppointmentsApi api = retrofitService.getRetrofit().create(AppointmentsApi.class);

        System.out.println("OK1.2");
        api.getSpecialisedDoc(specialization).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response)
            {
                //DONE : POST REQUEST result object (code, message)
                //if statement for successful response and not null body()
                System.out.println("Inside onResponse : OK2");
                if (!response.isSuccessful() || response.body() == null) {
                    System.out.println("Response Error: " + response.code() + " - " + response.message().toString());
                    return;
                }
                System.out.println("Response TEST 2000 : " + response.body().toString());
                Log.d("TAG 2000", "onResponse: " + response.body().toString());

                Result spec = response.body();
                System.out.println("spec : " + spec.toString());
                int status = spec.getStatus();
                System.out.println("Status : " + status);
                String message = spec.getMessage();
                System.out.println("Message : " + message);

                if (spec == null){
                    Log.d("BookAppointmentActivity - ", "Result object is null");
                    return;
                }

                if (status == 1){
                    Toast.makeText(PatientHomePageActivity.this, "Specialized Doc Doesn't Exist", Toast.LENGTH_LONG).show();
                    return;
                }

                if (message.isEmpty() || message == null){
                    Toast.makeText(PatientHomePageActivity.this, "No doctors found for the selected specialization", Toast.LENGTH_LONG).show();
                    return;
                }


                Gson gson = new Gson();
                Type doctorListType = new TypeToken<ArrayList<Doctor>>(){}.getType();
                doctorsBySpecList = gson.fromJson(message, doctorListType);
                System.out.println("doctorsBySpecList: " + doctorsBySpecList);
                docFullName.clear();
                //Test print
                for (Doctor doc : doctorsBySpecList) {
                    System.out.println("Doctor ID: " + doc.getDocId() + ", Name: " + doc.getDocFirstName() + " " + doc.getDocLastName());
                    selectedDocId = doc.getDocId();
                    docFullName.add(doc.getDocFirstName() + " " + doc.getDocLastName());
                }
                //TEST PRINTS
                System.out.println("[D2] ID : " + selectedDocId);
                System.out.println("[D2] docFAndLName : " + docFullName);
                popUpRecyclerAdapter = new PopUpRecyclerAdapter(doctorsBySpecList, PatientHomePageActivity.this);
                specPopUpRecycler.setAdapter(popUpRecyclerAdapter);
                popUpRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                System.out.println("onFailure: NOT OK");
                Toast.makeText(PatientHomePageActivity.this, "Request Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("onFailure", "onResponse: " + t.getMessage());
            }
        });
    }

    private void callAppointDetailsPopUp(Appointments appointments) {
        System.out.println("[Debug message] inside callAppointDetailsPopUp(): ");
        System.out.println("[Debug message] callAppointDetailsPopUp() appointment:" + appointments.toString());

        //Get Doctor instance: ID
        docInstance = Doctor.getInstance();
        docInstance.setDoctorData(appointments.getDoctor());
        if(docInstance == null){
            System.out.println("Null Object Reference during PopUp Dialog call");
            return;
        }

        //Cancel Pop Up Dialog
        appointDetPopUp = new Dialog(PatientHomePageActivity.this);
        appointDetPopUp.setContentView(R.layout.pat_appoint_det_popup_dialog);
        appointDetPopUp.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        appointDetPopUp.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_popup_background));
        appointDetPopUp.setCancelable(true); //when user clicks outside the dialog can not close

        //Pop Up Views
        //DONE: find views by id for text views
        docFNamePopUp = appointDetPopUp.findViewById(R.id.docFNameTxtPopUp);
        docLNamePopUp = appointDetPopUp.findViewById(R.id.docLNameTxtPopUp);
        docSpecPopUp = appointDetPopUp.findViewById(R.id.docSpecTxtPopUp);
        docPhoneNumPopUp = appointDetPopUp.findViewById(R.id.docPhoneNumTxtPopUp);
        docAddressPopUp = appointDetPopUp.findViewById(R.id.docAddressTxt);
        docCommPopUp = appointDetPopUp.findViewById(R.id.appCommentsTxtPopUp);
        rescheduleBtnPopUp = appointDetPopUp.findViewById(R.id.rschdlBtnPopUp);

        //Set data to the text views
        System.out.println("[Debug message] inside callAppointDetailsPopUp() docInstance: " + docInstance.getDocFirstName());
//        String fname = docInstance.getDocFirstName();
        docFNamePopUp.setText(docInstance.getDocFirstName());
        docLNamePopUp.setText(docInstance.getDocLastName());
        docSpecPopUp.setText(docInstance.getDocProfession());
        docPhoneNumPopUp.setText(docInstance.getDocPhoneNum());
        docAddressPopUp.setText(docInstance.getDocAddress());
        docCommPopUp.setText(appointments.getAppointInfo());

        //DONE: implement event listener onClick for reschedule BTN
        rescheduleBtnPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToReschedule = new Intent(PatientHomePageActivity.this, RescheduleAppointment.class);
                docInstance = Doctor.getInstance();
                docInstance.setDoctorData(appointments.getDoctor());
                System.out.println("Doctor Home Page: [TEST PRINT Reschedule] -> Patient selected = " + docInstance);
                System.out.println("Doctor home page before intent");
                goToReschedule.putExtra("appoint", appointments);
                startActivity(goToReschedule);
            }
        });

        // Show the dialog
        appointDetPopUp.show();
    }

    private List<SpecializationSet> getSetsOfSpecTitleAndIcon() {
        String[] specTitle = getResources().getStringArray(R.array.specialisations_array);
        TypedArray specIcons = getResources().obtainTypedArray(R.array.specialization_icons_set);
        List<SpecializationSet> specSetList = new ArrayList<>();
        for (int i=0; i<specTitle.length; i++){
            specSetList.add(new SpecializationSet(specTitle[i], specIcons.getDrawable(i)));
        }
        specIcons.recycle();
        return specSetList;
    }

    private void getAppointmentsInfo(Integer pID) {
        //TODO: Maybe it will be needed Singleton Pattern
        //TODO: call get request of get list of appointments of a specific user
        RetrofitService retrofitService = new RetrofitService();
        AppointmentsApi appointmentsApi = retrofitService.getRetrofit().create(AppointmentsApi.class);
        appointmentsApi.getAppointmentsByPatient(pID).enqueue(new Callback<List<Appointments>>() {
            @Override
            public void onResponse(Call<List<Appointments>> call, Response<List<Appointments>> response) {
                System.out.println("[-0-PatientHomePageActivity] onResponse: response BODY-> " + response.body());
                //TODO: set those data to the appointmentsList
                if (!response.isSuccessful() || response.body() == null) {
                    System.out.println("[-1-PatientHomePageActivity] onResponse: successful= no + body== null");
                    Toast.makeText(PatientHomePageActivity.this, "Failed to get patient's appointments list", Toast.LENGTH_SHORT).show();
                }

                System.out.println("[-2-PatientHomePageActivity] onResponse: response BODY2--> " + response.body());
                appointmentsList.addAll(response.body());
                patUsername.setText(pUsername);
                if (appointmentsList.isEmpty()) {
                    System.out.println("[-3-PatientHomePageActivity] onResponse: the appointmentsList is empty");
                    appointmentCarouselAdapter.notifyDataSetChanged();
                }
                appointmentCarouselAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Appointments>> call, Throwable throwable) {
                System.out.println("[-1-PatientHomePageActivity] onFailure: NOT OK");
                System.out.println("[-2-PatientHomePageActivity] onFailure - throwable.getMessage() : " + throwable.getMessage());
                Toast.makeText(PatientHomePageActivity.this, "Failed to fetch request" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}