package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Diagnosis;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.DiagnosisApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.DiagnosisRecordAdapter;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.LogInDTO;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.PDFGenerator;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.SearchRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiagnosisRecordsActivity extends AppCompatActivity {

    private RecyclerView diagnosisHistoryRecyclerView, searchRecyclerView;
    private DiagnosisRecordAdapter diagnosisRecordAdapter;
    private Dialog diagnosisDetailsPopUp;
    private TextView diagnType, diagnInstractions, treatmentName, treatmentDose, usernameTxt;
    private Button convertToPDFbtn;
    private List<Diagnosis> diagnosisList;
    private Patient patInstance;
    private LogInDTO userTypeInstance;
    private Integer userType;
    private Doctor docInstance;
    private PDFGenerator pdfGenerator;
    private List<Patient> patientList;

    /**Bottom navigation bar
     * Navigation based on user type*/
    private BottomNavigationView bottomNavigationView;
    private Class<?> targetHomePage, targetUserDetails;

    /**Search Bar*/
    private SearchView searchView;
    private SearchRecyclerAdapter searchRecyclerAdapter;
    /**Handling delayed API Request*/
    Handler handler = new Handler(Looper.getMainLooper());
    Runnable searchRunnable;
    private Patient selectedPatient, patInstanceSlctd;
    private String patAmka, fullNameP;
    private Integer patientID;

    /**Variables to change the to to bottom of attribute of diagnosisHistoryRecyclerView*/
    ConstraintLayout diagnRecConstrntLyt;
    ConstraintSet constraintSet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diagnosis_records);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        bottomNavigationView = findViewById(R.id.bottomNavBar);
//        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    /**Navigation to HomePage based on User Logged In (Doctor or Patient)*/
                    startActivity(new Intent(getApplicationContext() , targetHomePage));
                    finish();
                    return true;
                }else if (item.getItemId() == R.id.nav_manage_appointments) {
                    startActivity(new Intent(getApplicationContext() , ManageAppointmentsActivity.class));
                    finish();
                    return true;
                }else if (item.getItemId() == R.id.nav_diagn_history){
                    return true;
                }else if (item.getItemId() == R.id.nav_profile) {
                    /**Navigation to Details based on User Logged In (Doctor or Patient)*/
                    startActivity(new Intent(getApplicationContext(), targetUserDetails));
                    finish();
                    return true;
                }else{
                    return false;
                }
            }
        });

        usernameTxt = findViewById(R.id.usernameTxt);
        searchRecyclerView = findViewById(R.id.searchRcyclrView);
        searchView = findViewById(R.id.searchBar);
        diagnosisHistoryRecyclerView = findViewById(R.id.specRecyclerRecyclerView);
        diagnRecConstrntLyt = findViewById(R.id.diagnosisRecConstraintLayout);

        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        /**Based on User ID will be decided if the search bar is showing or not*/
        userTypeInstance = LogInDTO.getInstance();
        if (userTypeInstance == null){
            System.out.println("[*UserType] userTypeInstance == NULL");
            return;
        }
        System.out.println("[**UserType] userTypeInstance == " + userTypeInstance.getUserType());

        userType = userTypeInstance.getUserType();
        if (userType == 1){
            docInstance = Doctor.getInstance();
            System.out.println("TEST PRINT SOS 2 DOCTOR: "+ docInstance.getDocId());
            if (docInstance == null){
                System.out.println("[UserType 1] docInstance == null");
                return;
            }
            usernameTxt.setText(docInstance.getDocUsername());
            targetHomePage = DoctorHomePageActivity.class;
            targetUserDetails = DocDetails.class;
            System.out.println("[UserType 2] docInstance.getDocUsername()= " + docInstance.getDocUsername());
            System.out.println("[UserType 3] docInstance.getUserType()= " + docInstance.getUserType());
        } else if (userType == 2) {
            patInstance = Patient.getInstance();
            System.out.println("TEST PRINT SOS 2 PATIENT: "+ patInstance.getPatientId());
            if (patInstance == null){
                System.out.println("[UserType 4] patInstance == null");
                return;
            }
            usernameTxt.setText(patInstance.getPatUsername());
            targetHomePage = PatientHomePageActivity.class;
            targetUserDetails = PatDetails.class;
            System.out.println("[UserType 5] patInstance.getPatUsername()= " + patInstance.getPatUsername());
            System.out.println("[UserType 6] patInstance.getUserType()= " + patInstance.getUserType());
        }

//        patInstance = Patient.getInstance();
//        if (patInstance == null){
//            System.out.println("[1 DiagnHistory] Patient's Instance is NULL");
//            Log.d("DiagnosisRecordsActivity", "(1) onCreate: Patient instance --> NULL");
//            return;
//        }
//        System.out.println("[2 DiagnHistory] Patient's Instance is: " + patInstance.toString());
//        Log.d("DiagnosisRecordsActivity", "(2) onCreate: Patient instance --> " + patInstance.toString());

        System.out.println("[UserType 4] Initialize diagnosisList");
        diagnosisList = new ArrayList<>();
//        getDiagnosisOfPatient(patInstance.getPatientId());
        /**NEW CODE: FOR NEW FUNCTIONALITY*/
        if (userType == 1){
//            diagnosisList.clear(); // Ensure no diagnosis data is shown initially
//            diagnosisRecordAdapter.notifyDataSetChanged(); // Update the UI
            System.out.println("[UserType 5] condition check for user type: DOCTOR to initialize SearchBar component");
            System.out.println("[UserType 6] set VISIBLE Search View");
            searchView.setVisibility(View.VISIBLE);
            searchBarComponentVisibility(); // Make the search bar visible and functional
        }else if (userType == 2){
            System.out.println("[UserType 7] condition check for user type: PATIENT to call get request");

            //TODO: change this: app:layout_constraintTop_toBottomOf="@+id/searchBar" to be to usernameTxt
            constraintSet = new ConstraintSet();
            constraintSet.clone(diagnRecConstrntLyt); //clone current layout

            /**Update RecyclerView Top Constraint Layout to below usernameTxt*/
            constraintSet.connect(R.id.specRecyclerRecyclerView,
                    ConstraintSet.TOP,
                    R.id.usernameTxt,
                    ConstraintSet.BOTTOM);

            constraintSet.applyTo(diagnRecConstrntLyt);
            getDiagnosisOfPatient(patInstance.getPatientId());
        }



        diagnosisHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //TODO: Call get Request of Get All Diagnosis
        diagnosisRecordAdapter = new DiagnosisRecordAdapter(diagnosisList, this, new DiagnosisRecordAdapter.OnBtnListener() {
            @Override
            public void onDiagnosisButtonClick(Diagnosis diagnosis) {
                //TODO: callPopUpDialog(diagnosis);
                callDiagnosisDetailsPopUp(diagnosis);
                //TODO: popUp.show();
                diagnosisDetailsPopUp.show();
            }
        });
        diagnosisHistoryRecyclerView.setAdapter(diagnosisRecordAdapter);

//--------------------SEARCH VIEW IMPLEMENTATION -----------------------
        //TODO: Add if condition weather the userType == 1 : Doctor Logged In

    }

    private void callDiagnosisDetailsPopUp(Diagnosis diagnosis) {
        diagnosisDetailsPopUp = new Dialog(DiagnosisRecordsActivity.this);
        diagnosisDetailsPopUp.setContentView(R.layout.manage_selected_appointment_popup_dialog);
        diagnosisDetailsPopUp.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        diagnosisDetailsPopUp.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_popup_background));
        diagnosisDetailsPopUp.setCancelable(true);//when user clicks outside the dialog can not close

        //TODO: Declare Components for TextViews
        diagnType = diagnosisDetailsPopUp.findViewById(R.id.diagnTypeCardTxt);
        diagnInstractions = diagnosisDetailsPopUp.findViewById(R.id.diagnInstructionsCardTxt);
        treatmentName = diagnosisDetailsPopUp.findViewById(R.id.treatNameCardTxt);
        treatmentDose = diagnosisDetailsPopUp.findViewById(R.id.treatDoseCardTxt);


        //TODO: set context to TextViews
        diagnType.setText(diagnosis.getDiagnType());
        diagnInstractions.setText(diagnosis.getDiagnInfo());
        treatmentName.setText(diagnosis.getTreatment());
        treatmentDose.setText(diagnosis.getTreatmDose());

        convertToPDFbtn = diagnosisDetailsPopUp.findViewById(R.id.convertToPDFbtn);

        convertToPDFbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfGenerator = new PDFGenerator(DiagnosisRecordsActivity.this);
                pdfGenerator.generatePDF(diagnosis.getPatient(), diagnosis.getDoctor(), diagnosis);
                Toast.makeText(DiagnosisRecordsActivity.this, "PDF File Generated Successfully", Toast.LENGTH_SHORT).show();
                diagnosisDetailsPopUp.dismiss();
            }
        });
    }

    private void getDiagnosisOfPatient(Integer patientId) {
        RetrofitService retrofitService = new RetrofitService();
        DiagnosisApi diagnosisApi = retrofitService.getRetrofit().create(DiagnosisApi.class);
        diagnosisApi.getListOfDiagnosisByPatient(patientId).enqueue(new Callback<List<Diagnosis>>() {
            @Override
            public void onResponse(Call<List<Diagnosis>> call, Response<List<Diagnosis>> response) {
                System.out.println("[DiagnosisRecordsActivity] onResponse 1 : response BODY -> " + response.body());
                diagnosisList.clear();
                if (!response.isSuccessful() || response.body() == null) {
                    System.out.println("[DiagnosisRecordsActivity] onResponse 2 : isSuccessful() => no + body() => null");
                    Toast.makeText(DiagnosisRecordsActivity.this, "Failed to get doctor's diagnosis list", Toast.LENGTH_SHORT).show();
                    return;
                }

                System.out.println("[DiagnosisRecordsActivity] onResponse 3 : response BODY 2 --> " + response.body());
                diagnosisList.addAll(response.body());
                //patUsername.setText(pUsername);
                if (diagnosisList.isEmpty()) {
                    System.out.println("[DiagnosisRecordsActivity] onResponse 4 : the diagnosisList is empty");
                }
                System.out.println("[DiagnosisRecordsActivity] LIST SIZE IN RESPONSE: " + diagnosisList.size());
                diagnosisRecordAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Diagnosis>> call, Throwable throwable) {
                System.out.println("[DiagnosisRecordsActivity] onFailure: NOT OK");
                System.out.println("[DiagnosisRecordsActivity] onFailure - throwable.getMessage() : " + throwable.getMessage());
                Toast.makeText(DiagnosisRecordsActivity.this, "Failed to fetch request" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchBarComponentVisibility() {
        //Search View Session
        searchView.clearFocus();
        System.out.println(">[DiagnRec 01] : before setOnQueryTextFocusChangeListener");

        /**When the user clicks the search bar change
         * visibility of recycler view component on layout file*/
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    System.out.println(">[DiagnRec 1.1] : set VISIBLE searchRecyclerView if focus == " + hasFocus);
                    searchRecyclerView.setVisibility(View.VISIBLE);
                }else {
                    searchRecyclerView.setVisibility(View.INVISIBLE);
                }
            }
        });

        System.out.println(">[DiagnRec 02] : before setOnQueryTextListener");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String incomingAmka) {
                System.out.println(">[DiagnRec 03] : inside onQueryTextChange");
                //DONE: Delayed API call
                System.out.println(">[DiagnRec 04] : before if -> searchRunnable != null ???");
                if (searchRunnable != null){
                    handler.removeCallbacks(searchRunnable);
                }
                System.out.println(">[DiagnRec 05] : before run() implementation");
                searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (!incomingAmka.isEmpty()){
                            //DONE: method to implement the API call
                            searchPatient(incomingAmka);  //-> incomingAmka is the amka that the user is typing into the search view
                        }else {
                            searchRecyclerView.setVisibility(View.GONE);
                            searchRecyclerAdapter.updatePatList(new ArrayList<>());
                        }
                    }
                };
                handler.postDelayed(searchRunnable, 500);
                return false;
            }
        });

//--------------------SETTING RECYCLER LAYOUT MANAGER-----------------------
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        searchRecyclerView.setLayoutManager(layoutManager);

        patientList = new ArrayList<>();

        // Initialize the adapter with the click listener
        searchRecyclerAdapter = new SearchRecyclerAdapter(DiagnosisRecordsActivity.this, patientList, new SearchRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Patient patient) {
                selectedPatient = patient;
                System.out.println(">[DiagnRec 06] onItemClick() inside -> searchRecyclerAdapter: " + selectedPatient);
                Toast.makeText(DiagnosisRecordsActivity.this, "Selected: " + patient.getPatFirstName(), Toast.LENGTH_SHORT).show();

                // Optionally, hide the RecyclerView after selecting an item
                searchRecyclerView.setVisibility(View.INVISIBLE);

                /**NEW CODE: FOR NEW FUNCTIONALITY*/
                // Fetch and display diagnoses for the selected patient
                if (selectedPatient != null) {
                    getDiagnosisOfPatient(selectedPatient.getPatientId());
                }
                searchView.clearFocus();
            }
        });
        searchRecyclerView.setAdapter(searchRecyclerAdapter);
    }

    //API Get Request: search patient and having dynamic data in recycler view
    private void searchPatient(String amka) {
        //TODO: retrofit, diagnosis api, diagnosis (entity)
        RetrofitService retrofitService = new RetrofitService();
        DiagnosisApi diagnosisApi = retrofitService.getRetrofit().create(DiagnosisApi.class);
        //TODO: API call for endpoint: searchPatByAmka -> get request
        diagnosisApi.searchPatByAmka(amka).enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if (!response.isSuccessful() || response.body() == null){
                    /**NEW CODE: FOR NEW FUNCTIONALITY*/
                    searchRecyclerAdapter.updatePatList(new ArrayList<>());
                    System.out.println(">[DiagnRec 07] searchPatient()-> if for response " + response.body() + " : " + response.isSuccessful());
                    Toast.makeText(DiagnosisRecordsActivity.this, "No user Found", Toast.LENGTH_SHORT).show();
//                    recyclerView.setVisibility(View.GONE);
                    return;
                }

                System.out.println(">[DiagnRec 08] searchPatient()-> response.body(): " + response.body());
                System.out.println(">[DiagnRec 09] searchPatient()-> Assign response body to patient");
                Patient responsePat = response.body();

                if (responsePat == null){
                    System.out.println(">[DiagnRec 10] searchPatient()-> responsePat is NULL");
                    return;
                }

                selectedPatient = Patient.getInstance();
                System.out.println(">[DiagnRec 11] searchPatient()-> matchedPatientInstance: " + selectedPatient);
                if (selectedPatient == null){
                    System.out.println(">[DiagnRec 12] searchPatient() inside if-> matchedPatientInstance: " + selectedPatient);
                    Toast.makeText(DiagnosisRecordsActivity.this, "No user Found", Toast.LENGTH_SHORT).show();
//                    recyclerView.setVisibility(View.GONE);
                    return;
                }

                /**NEW CODE: FOR NEW FUNCTIONALITY*/
                //Set all data from the response: which is a Patient Object
                selectedPatient.setPatientData(responsePat);

                System.out.println(">[DiagnRec 14] searchPatient()-> matchedPatientInstance: " +
                        selectedPatient.getPatientId() + " " + //this is what i need to send for the diagnosis creation
                        selectedPatient.getPatFirstName() + " " +
                        selectedPatient.getPatLastName() + " " +
                        selectedPatient.getPatSecuredNum());
                //TODO: add all these data to the list appeared in the recycler view
                ArrayList<Patient> matchedPatList = new ArrayList<>();
                matchedPatList.add(selectedPatient);
                if (matchedPatList.isEmpty()){
                    System.out.println(">[DiagnRec 15] searchPatient() inside if-> matchedPatList: " + matchedPatList);
                    Toast.makeText(DiagnosisRecordsActivity.this, "matchedPatList = null", Toast.LENGTH_SHORT).show();
//                    recyclerView.setVisibility(View.GONE);
                    return;
                }
                /**NEW CODE: FOR NEW FUNCTIONALITY*/
                searchRecyclerAdapter.updatePatList(matchedPatList);
                searchRecyclerView.setVisibility(View.VISIBLE);

                System.out.println(">[DiagnRec 16] searchPatient()-> matchedPatList: " + matchedPatList);
//                recyclerView.setVisibility(View.VISIBLE);
                System.out.println(">[DiagnRec 17] searchPatient(): after recyclerView Visibility and before updatePatList() of [SrchRclrAdptr]");
//                searchRecyclerAdapter.updatePatList(matchedPatList);
                System.out.println(">[DiagnRec 18] searchPatient() -> after updateList() of adapter");
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable throwable) {
                System.out.println(">[DiagnRec 19] searchPatient()-> onFailure: NOT OK");
                Toast.makeText(DiagnosisRecordsActivity.this, "Failed to fetch request" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    //Get selected patient's data -> to set them in components of current activity
    //this data must be sent to the Recycler viw adapter used in this activity
//        //Setting data from matched patient to local class variables, for use in create diagnosis API request
//        patInstanceSlctd = Patient.getInstance();
//        if (patInstanceSlctd != null) {
//            patientID = patInstanceSlctd.getPatientId();
//            fullNameP = patInstanceSlctd.getPatFirstName().concat(" ").concat(patInstanceSlctd.getPatLastName());
//            patAmka = patInstanceSlctd.getPatSecuredNum();
//            System.out.println(">[DiagnRec] searchPatient()-> matchedPatientInstance: " + patientID + " " + fullNameP + " " + patAmka);
//        }
//
//        System.out.println(">[DiagnRec] searchPatient()-> matchedPatientInstance: " +
//                patInstanceSlctd.getPatientId() + " " + //this is what i need to send for the diagnosis creation
//                patInstanceSlctd.getPatFirstName() + " " +
//                patInstanceSlctd.getPatLastName() + " " +
//                patInstanceSlctd.getPatSecuredNum());

//        //Set selected patients full name and amka
//        fullNamePatTxt.setText(fullNameP);
//        amkaPatTxt.setText(patAmka);

//        TEST CODE
//        fullNamePatTxt.setText("Antouela Bitsa");
//        amkaPatTxt.setText("123456789");
//        patientID = 1;

}