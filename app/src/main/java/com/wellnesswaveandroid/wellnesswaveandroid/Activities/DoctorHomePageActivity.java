package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.AppointmentsApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.CustomZoomLayoutManager;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.DocAppointmentCarouselAdapter;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorHomePageActivity extends AppCompatActivity {

    //UI Elements Declaration
    //Upcoming Appointments Carousel
    private TextView usernameTxt;
    private TextView fullNamePatTxt, amkaPatTxt, dateAppointPatTxt, timeAppointPatTxt, detailsAppointPatTxt;
    private Button diagnosisBtn, rescheduleBtn, cancelBtn, popUpNoBtn, popUpYesBtn;
    private RecyclerView appointDocRecyclerView;
    private BottomNavigationView bottomNavigationView;
    private Dialog cancelPopoUpDialog;
    //Manage Appointments Vertical Carousel
//    private RecyclerView manageAppointRecyclerView;
//    private RecyclerView patInfoOnAppointmentRecycler;

    //Adapter Declaration
    private DocAppointmentCarouselAdapter docAppointmentCarouselAdapter;
//    private DocManageAppointmentsAdapter docManageAppointmentsAdapter;
//    private PatInfoAdapter patInfoAdapter;

    //Helpful Variables
    private Doctor docInstance;
    private Patient patInstance, selectedPatient, patientInstance;
    private Integer docID, patID, appointID;
    private String fullNamePat;
    private List<Appointments> docAppointmentsList;
//    private List<Appointments> docManageAppointmentsList;
    private Appointments nextAppoint, selectedAppointment;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home_page);

        bottomNavigationView = findViewById(R.id.bottomNavBar);
//        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    return true;
                }else if (item.getItemId() == R.id.nav_manage_appointments) {
                    startActivity(new Intent(getApplicationContext() , ManageAppointmentsActivity.class));
                    //if there are transitions=> overridePendingTransition()
                    finish();
                    return true;
                }else if (item.getItemId() == R.id.nav_diagn_history){
                    startActivity(new Intent(getApplicationContext(), DiagnosisRecordsActivity.class));
                    finish();
                    return true;
                }else if (item.getItemId() == R.id.nav_profile) {
                    startActivity(new Intent(getApplicationContext(), DocDetails.class));
                    //if there are transitions=> overridePendingTransition()
                    finish();
                    return true;
                }else{
                    return false;
                }
            }
        });

        usernameTxt = findViewById(R.id.usernameDocTxt);
        appointDocRecyclerView = findViewById(R.id.appointmentDocRecycler);
        //manageAppointRecyclerView = findViewById(R.id.manageAppointRecycler);
        fullNamePatTxt = findViewById(R.id.fullNameAppointPatTxt);
        amkaPatTxt = findViewById(R.id.amkaAppointPatTxt);
        dateAppointPatTxt = findViewById(R.id.dateAppointPatTxt);
        timeAppointPatTxt = findViewById(R.id.timeAppointPatTxt);
        detailsAppointPatTxt = findViewById(R.id.detailsAppointPatTxt);
        diagnosisBtn =  findViewById(R.id.diagnsisBtn);
        rescheduleBtn = findViewById(R.id.rescheduleAppointBtn);
        cancelBtn = findViewById(R.id.cancelAppointBtn);

        //Get Doctor instance: ID
        docInstance = Doctor.getInstance();
        System.out.println("TEST PRINT SOS 2 DOCTOR: "+ docInstance.getDocId());
        if (docInstance!=null){
            usernameTxt.setText(docInstance.getDocUsername());
            docID = docInstance.getDocId();
        }else{
            System.out.println("[1-DocHomePage] docInstance == null");
        }

        //Get Patient Instance: ID
        patInstance = Patient.getInstance();
        if (patInstance!=null){
            patID = patInstance.getPatientId();
        }else{
            System.out.println("[1-DocHomePage] docInstance == null");
        }

        System.out.println("--------------Print of nextAppoint List----------- => " + selectedAppointment);

        //Cancel Pop Up Dialog
        cancelPopoUpDialog = new Dialog(DoctorHomePageActivity.this);
        cancelPopoUpDialog.setContentView(R.layout.cancel_appoint_popup_dialog);
        cancelPopoUpDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cancelPopoUpDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_popup_background));
        cancelPopoUpDialog.setCancelable(true); //when user clicks outside the dialog can not close

        popUpNoBtn = cancelPopoUpDialog.findViewById(R.id.rescheduleBtn);
        popUpYesBtn = cancelPopoUpDialog.findViewById(R.id.rschdlBtnPopUp);

        popUpYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DONE: business logic on cancellation for API Request
                appointID = selectedAppointment.getAppointmentId();
                updateCurrentAppointStatus(appointID, selectedAppointment.setStatus("canceled"));
                deleteAppoint(appointID);
                cancelPopoUpDialog.dismiss();
            }
        });

        popUpNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                navigateToRescheduleAppointment();
                cancelPopoUpDialog.dismiss();
            }
        });

        //Upcoming Appointments Carousel
        docAppointmentsList = new ArrayList<>();
        docAppointmentCarouselAdapter = new DocAppointmentCarouselAdapter(docAppointmentsList, this,
                new DocAppointmentCarouselAdapter.OnInfoBtnListener() {
            @Override
            public void onInfoButtonClick(Appointments appoint) {
                selectedAppointment = appoint;
                System.out.println("[DEBUG PRINT] selected Appoint data: " + selectedAppointment.toString());
                selectedPatient = appoint.getPatient();
                System.out.println("[DEBUG PRINT] selected Pateint data: " + selectedPatient.getPatientId());
                setDataToDetailsLayout(appoint);
            }
        });
        appointDocRecyclerView.setAdapter(docAppointmentCarouselAdapter);
        //Implementation of GET Request
        getDocAppointmentInfo(docInstance.getDocId());
        System.out.println("SOS PRINT TEST 1 LIST PRINT: "+ docAppointmentsList);
        appointDocRecyclerView.setLayoutManager(new CustomZoomLayoutManager(this));

        if (docAppointmentsList.isEmpty()){
            Log.d("RecyclerView", "LinearLayoutManager applied.");
            appointDocRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            Log.d("RecyclerView", "Data set updated. Item count: " + docAppointmentsList.size());
            docAppointmentCarouselAdapter.notifyDataSetChanged();
        }else {
            Log.d("RecyclerView", "CustomZoomLayoutManager applied.");
            appointDocRecyclerView.setLayoutManager(new CustomZoomLayoutManager(this));
            docAppointmentCarouselAdapter.notifyDataSetChanged();
        }

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(appointDocRecyclerView);


        //OnClick Listener to navigate to Insert Diagnosis Activity
        //DONE: second type of Insert Diagnosis
        diagnosisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAppointment == null || selectedPatient == null){
                    Toast.makeText(DoctorHomePageActivity.this, "Please select Appointment first", Toast.LENGTH_SHORT).show();
                    return;
                }
                navigateToInsertDiagnosis();
            }
        });

        //OnClick Listener to navigate to Reschedule Appointment Activity
        //DONE: business logic in Reschedule Appointment Activity java class
        rescheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAppointment == null || selectedPatient == null){
                    Toast.makeText(DoctorHomePageActivity.this, "Please select Appointment first", Toast.LENGTH_SHORT).show();
                    return;
                }
                navigateToRescheduleAppointment();
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelPopoUpDialog.show();
            }
        });
    }

    private void deleteAppoint(Integer appointID) {
        System.out.println("[Rschdl 1] Inside rescheduleAPICall() before retrofit initialization -> appointment = " + appointID);
        RetrofitService retrofitAppointSrvc = new RetrofitService();
        System.out.println("[Rschdl 1.2] API initialization");
        AppointmentsApi appointmentsApi = retrofitAppointSrvc.getRetrofit().create(AppointmentsApi.class);
//        appointID = 11; --> debug
        appointmentsApi.deleteAppointment(appointID).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                System.out.println("[Rschdl 1.3] Inside on Response: " + response.body() + " => " + response.isSuccessful());
                if (response.isSuccessful()){
                    Log.d("[APPOINT 1] TAG:" + "SUCCESS: ", "onResponse: " + response.body());
                    Toast.makeText(DoctorHomePageActivity.this, "You have canceled this appointment successfully", Toast.LENGTH_LONG).show();
                }else {
                    Log.d("[APPOINT 1] TAG:" + "FAIL: ", "onResponse: FAILED " + response.body() + " " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(DoctorHomePageActivity.this, "Request Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("[APPOINT 2] TAG: " + " onFailure: ", "The Message is : " + t.getMessage());
            }
        });

    }

    private void updateCurrentAppointStatus(Integer appointId, String status){
        System.out.println("[Rschdl 2] Inside updateCurrentAppointStatus() before retrofit initialization -> appointmentID = " + appointId);
        RetrofitService retrofitAppointSrvc = new RetrofitService();
        System.out.println("[Rschdl 2.2] API initialization");
        AppointmentsApi appointmentsApi = retrofitAppointSrvc.getRetrofit().create(AppointmentsApi.class);
        appointmentsApi.updateAppointOnReschedule(appointId, status).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                System.out.println("[Rschdl 2.3] Inside on Response: " + response.body() + " => " + response.isSuccessful());
                if (response.isSuccessful()){
                    Log.d("[APPOINT UPDATED] TAG:" + " SUCCESS: ", "onResponse: " + response.body());
//                    Toast.makeText(RescheduleAppointment.this, "You have rescheduled this appointment successfully", Toast.LENGTH_LONG).show();
                }else {
                    Log.d("[APPOINT UPDATED] TAG:" + " FAIL: ", "onResponse: FAILED " + response.body() + " " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(DoctorHomePageActivity.this, "Request Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("[APPOINT UPDATED] TAG: " + " onFailure: ", "The Message is : " + t.getMessage());
            }
        });
    }

    private void navigateToRescheduleAppointment() {
        Intent goToReschedule = new Intent(DoctorHomePageActivity.this, RescheduleAppointment.class);
        patientInstance = Patient.getInstance();
        patientInstance.setPatientData(selectedPatient);
        System.out.println("Doctor Home Page: [TEST PRINT Reschedule] -> Patient selected = " + patientInstance);
        System.out.println("Doctor home page before intent");
        goToReschedule.putExtra("appoint", selectedAppointment);
        goToReschedule.putExtra("patient", patientInstance);
        startActivity(goToReschedule);
    }

    //Navigation method to Insert Diagnosis Activity
    private void navigateToInsertDiagnosis() {
        Intent goToInsertDiagnosis = new Intent(DoctorHomePageActivity.this, InsertDiagnosisActivity.class);
        patientInstance = Patient.getInstance();
        patientInstance.setPatientData(selectedPatient);
        System.out.println("Doctor Home Page: [TEST PRINT] -> Patient selected = " + patientInstance);
        goToInsertDiagnosis.putExtra("appoint", selectedAppointment);
        goToInsertDiagnosis.putExtra("patient", patientInstance);
        startActivity(goToInsertDiagnosis);
    }

//    private void getNextAppointment(List<Appointments> appointList) {
//        Collections.sort(appointList, new Comparator<Appointments>() {
//            @Override
//            public int compare(Appointments appoint1, Appointments appoint2) {
//                //return appoint1.getTime().compareTo(appoint2.getTime());
//                try {
//                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
//                    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
//
//                    Date firstDate = sdfDate.parse(appoint1.getDate());
//                    Date secondDate = sdfDate.parse(appoint2.getDate());
//
//                    int comparedDate = firstDate.compareTo(secondDate);
//                    if (comparedDate == 0){
//                        Date firstTime = sdfTime.parse(appoint1.getTime());
//                        Date secondTime = sdfTime.parse(appoint2.getTime());
//                        return firstTime.compareTo(secondTime);
//                    }
//                    return comparedDate;
//                } catch (ParseException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//
//        System.out.println("sorted list of appointment: " + appointList);
//
//        if (appointList.size()>1){
//            nextAppoint = appointList.get(1);
//            System.out.println("DoctorHomePage: [TEST PRINT]-> Next appoint = " + nextAppoint);
//        }else {
//            nextAppoint = appointList.get(0);
//            System.out.println("List does not have a second appointment" + nextAppoint);
//        }

        //Get second record of the list
//        for (int i=0; i<appointList.size(); i++){
//            nextAppoint = appointList.get(i++);
//            break;
//        }
//
//        System.out.println("-->> [DocHomePage 1] Sorted list: " + nextAppoint);
//
//        //Set the value to each text view
//        setDataToDetailsLayout(nextAppoint);
//
//        //set the patients id
//        selectedPatient = nextAppoint.getPatient();
//    }

    private void setDataToDetailsLayout(Appointments nextAppoint) {
        fullNamePatTxt.setText(concatFullName(nextAppoint.getPatient().getPatFirstName(), nextAppoint.getPatient().getPatLastName()));
        amkaPatTxt.setText(nextAppoint.getPatient().getPatSecuredNum());
        dateAppointPatTxt.setText(nextAppoint.getDate());
        timeAppointPatTxt.setText(nextAppoint.getTime());
        detailsAppointPatTxt.setText(nextAppoint.getAppointInfo());
    }


    private void getDocAppointmentInfo(Integer dID) {
        System.out.println("Inside manage call request");
        System.out.println("SOS PRINT 3 ID INSIDE REQUEST METHOD: "+dID);
        RetrofitService retrofitService = new RetrofitService();
        AppointmentsApi appointmentsApi = retrofitService.getRetrofit().create(AppointmentsApi.class);
        System.out.println("SOS PRINT 4 -->before get request call");
        appointmentsApi.getAppointmentsByDoctor(dID).enqueue(new Callback<List<Appointments>>() {
            @Override
            public void onResponse(Call<List<Appointments>> call, Response<List<Appointments>> response) {
                System.out.println("[-0-DoctorHomePageActivity] onResponse: response BODY-> " + response.body());
                docAppointmentsList.clear();

                if (!response.isSuccessful() || response.body() == null) {
                    System.out.println("[-1-DoctorHomePageActivity] onResponse: successful= no + body== null");
                    Toast.makeText(DoctorHomePageActivity.this, "Failed to get doctor's appointments list", Toast.LENGTH_SHORT).show();
                    docAppointmentCarouselAdapter.notifyDataSetChanged();
                }

                //DONE: set those data to the appointmentsList
                System.out.println("[-2-DoctorHomePageActivity] onResponse: response BODY2--> " + response.body());
                docAppointmentsList.addAll(response.body());
                //patUsername.setText(pUsername);
                if (docAppointmentsList.isEmpty()) {
                    System.out.println("[-3-DoctorHomePageActivity] onResponse: the appointmentsList is empty");
                    docAppointmentCarouselAdapter.notifyDataSetChanged();
                }
//                else{
//                    getNextAppointment(docAppointmentsList);
//                    System.out.println("LIST SIZE IN RESPONSE: " + docAppointmentsList.size());
//                }
                docAppointmentCarouselAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Appointments>> call, Throwable throwable) {
                System.out.println("[-1-DoctorHomePageActivity] onFailure: NOT OK");
                System.out.println("[-2-DoctorHomePageActivity] onFailure - throwable.getMessage() : " + throwable.getMessage());
                Toast.makeText(DoctorHomePageActivity.this, "Failed to fetch request" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String concatFullName(String patFirstName, String patLastName) {
        return  patFirstName.concat(" ").concat(patLastName);
    }
}