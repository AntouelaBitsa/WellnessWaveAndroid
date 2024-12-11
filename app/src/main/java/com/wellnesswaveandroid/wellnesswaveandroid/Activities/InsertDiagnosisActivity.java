package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Diagnosis;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.DiagnosisApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertDiagnosisActivity extends AppCompatActivity {
    //Components v2
    //private SearchView searchView;
    private BottomNavigationView bottomNavigationView;
    private TextInputEditText diagnosisEdt, treatmentEdt, doseEdt, infoDiEdt;
    private Button startDateBtn, endDateBtn, saveDiagn;
    private ImageView extractToPDF;
    private TextView fullNamePatTxt, amkaPatTxt;
    private MaterialCheckBox enableFieldsChckBox;

    //Recycler View
    //private RecyclerView recyclerView;
    //private SearchRecyclerAdapter searchRecyclerAdapter;
    //private ArrayList<Patient> patientList;
    private Patient patientInstanceSlctd;
    private Doctor docInstance;

    //Handling delayed API Request
    //Handler handler = new Handler(Looper.getMainLooper());
    //Runnable searchRunnable;

    //Data after the selection of user
    //private Patient selectedPatient;
    private Integer patientID, doctorID;
    private String patAmka, fullNameP;
    private String diagnosis, treatment, dose, info;

    //Date Picker variables
    private String formattedStartDate, formattedEndDate;

    //Diagnosis Object For Post Request
    private Diagnosis diagnosisReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_diagnosis);

        bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(getApplicationContext(), DoctorHomePageActivity.class));
                    finish();
                    return true;
                }else if (item.getItemId() == R.id.nav_profile) {
                    startActivity(new Intent(getApplicationContext(), DocDetails.class));
                    //if there are transitions=> overridePendingTransition()
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.nav_manage_appointments) {
//                        startActivity(new Intent(getApplicationContext() /*, Manage Appointments class */));
                    //if there are transitions=> overridePendingTransition()
//                        finish();
                    return true;
                }else {
                    return false;
                }
            }
        });

        //TODO: Search Bar-> search patient based on AMKA and select a patient
        System.out.println(">[InsrtDgn 01] onCreate : insert diagnosis HERE");

        //Components Initialization
//        recyclerView = findViewById(R.id.patRecyclerView);
//        searchView = findViewById(R.id.patSearchBar);
        diagnosisEdt = findViewById(R.id.prefilledAppointEdt);
        treatmentEdt = findViewById(R.id.treatementDiEdt);
        doseEdt = findViewById(R.id.doseDiEdt);
        startDateBtn = findViewById(R.id.dateReBtn);
        endDateBtn = findViewById(R.id.timeReBtn);
        infoDiEdt = findViewById(R.id.prefilledDateEdt);
        saveDiagn = findViewById(R.id.saveDiagnBtn);
        extractToPDF = findViewById(R.id.extractToPDFImg);
        fullNamePatTxt = findViewById(R.id.patFullNameReTxt);
        amkaPatTxt = findViewById(R.id.patAmkaReTxt);
        enableFieldsChckBox = findViewById(R.id.activateChckBx);


//--------------------COMMENTS FOR THODORIS-----------------------
//--------------------GETTING DOCTOR INSTANCE: SINGLETON PATTERN-----------------------
        //TODO: get connected doctors id
        docInstance = Doctor.getInstance();
        if (docInstance != null) {
            //--------------------HERE DOCTOR_ID HAS ONLY ID AS A VALUE-----------------------
            //--------------------WHILE IN OBJECT CREATION IT PASSES ALL THE OBJECT-----------------------
            //--------------------THE ISSUE DOESN'T SEEM TO BE THIS ONE THOUGH-----------------------
            doctorID = docInstance.getDocId();
            System.out.println("////////////////doctorID => " + doctorID);
        }else{
            System.out.println("[SOS ID] docInstance == null");
        }

        //TODO: get selected patient's data from instance
        //Setting data from matched patient to local class variables, for use in create diagnosis API request
//        patientInstanceSlctd = Patient.getInstance();
//        if (patientInstanceSlctd != null) {
//            patientID = patientInstanceSlctd.getPatientId();
//            fullNameP = concatFullName(patientInstanceSlctd.getPatFirstName(), patientInstanceSlctd.getPatLastName());
//            patAmka = patientInstanceSlctd.getPatSecuredNum();
//            System.out.println(">[InsrtDgn 05.1] searchPatient()-> matchedPatientInstance: " + patientID + " " + fullNameP + " " + patAmka);
//        }
//
//        System.out.println(">[InsrtDgn 06] searchPatient()-> matchedPatientInstance: " +
//                patientInstanceSlctd.getPatientId() + " " + //this is what i need to send for the diagnosis creation
//                patientInstanceSlctd.getPatFirstName() + " " +
//                patientInstanceSlctd.getPatLastName() + " " +
//                patientInstanceSlctd.getPatSecuredNum());
//
//        //Set selected patients full name and amka
//        fullNamePatTxt.setText(fullNameP);
//        amkaPatTxt.setText(patAmka);

//        TEST CODE
//        fullNamePatTxt.setText("Antouela Bitsa");
//        amkaPatTxt.setText("123456789");
//        patientID = 1;

        Intent getFromHopePage = getIntent();
        Patient patientObj = (Patient) getFromHopePage.getSerializableExtra("patient");
        System.out.println("[Reschedule *1*] intent data: " + patientObj.getPatFirstName());

        System.out.println(">[InsrtDgn 01.1 onCreate]: doctorID= " + doctorID);
        patientInstanceSlctd = Patient.getInstance();
        patientInstanceSlctd.setPatientData(patientObj);
        if (patientInstanceSlctd != null){
            patAmka = patientInstanceSlctd.getPatSecuredNum();
            fullNameP = concatFullName(patientInstanceSlctd.getPatFirstName(), patientInstanceSlctd.getPatLastName());
            patientID = patientInstanceSlctd.getPatientId();
        }else{
            System.out.println("Failed to fetch selected patient");
            return;
        }
        fullNamePatTxt.setText(fullNameP);
        amkaPatTxt.setText(patAmka);
        System.out.println("Insert Diagnosis: [TEST PRINT one]-> selectedPatient = " + patientInstanceSlctd.getPatientId() +" " + patientInstanceSlctd.getPatLastName()
                +" "+ patientInstanceSlctd.getPatSecuredNum());
        System.out.println("Insert Diagnosis: [TEST PRINT]-> amka = " + patAmka + " " + amkaPatTxt.getText());
        System.out.println("Insert Diagnosis: [TEST PRINT]-> fullName = " + fullNameP);


//--------------------COMMENTS FOR THODORIS START-----------------------
//--------------------SEARCH VIEW IMPLEMENTATION -----------------------
        //Search View Session
//        searchView.clearFocus();
//        System.out.println(">[InsrtDgn 02 onCreate] : before setOnQueryTextFocusChangeListener");
//        //Classic way
//        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                //When the user clicks the search bar change visibility of recycler view component on layout file
//                if(hasFocus){
//                    recyclerView.setVisibility(View.VISIBLE);
//                }else {
//                    recyclerView.setVisibility(View.GONE);
//                }
//            }
//        });

//        System.out.println(">[InsrtDgn 03 onCreate] : before setOnQueryTextListener");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                System.out.println(">[InsrtDgn 04 onCreate] : inside onQueryTextChange");
////                fileList(newText); //for static data
//                //DONE: Delayed API call
//                System.out.println(">[InsrtDgn 05 onCreate] : before if -> searchRunnable != null ???");
//                if (searchRunnable != null){
//                    handler.removeCallbacks(searchRunnable);
//                }
//                System.out.println(">[InsrtDgn 06 onCreate] : before run() implementation");
//                searchRunnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!newText.isEmpty()){
//                            //DONE: method to implement the API call
//                            searchPatient(newText);  //-> newText is the amka that the user is typing into the search view
//                        }else {
//                            recyclerView.setVisibility(View.GONE);
//                            searchRecyclerAdapter.updatePatList(new ArrayList<>());
//                        }
//                    }
//                };
//                handler.postDelayed(searchRunnable, 500);
//                return false;
//            }
//        });

//--------------------SETTING RECYCLER LAYOUT MANAGER-----------------------
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        patientList = new ArrayList<>();
//
//        // Initialize the adapter with the click listener
//        searchRecyclerAdapter = new SearchRecyclerAdapter(this, patientList, new SearchRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Patient patient) {
//                selectedPatient = patient;
//                System.out.println(">[InsrtDgn 00] onItemClick() inside -> searchRecyclerAdapter: " + selectedPatient);
//                Toast.makeText(InsertDiagnosisActivity.this, "Selected: " + patient.getPatFirstName(), Toast.LENGTH_SHORT).show();
//
//                // Optionally, hide the RecyclerView after selecting an item
//                recyclerView.setVisibility(View.GONE);
//            }
//        });
//        recyclerView.setAdapter(searchRecyclerAdapter);

        //here where the edit text context set to string

        //TODO: Set Date Picker in buttons->  startDateBtn, endDateBtn
        //TODO: Get content from date selection button
        //--------------------Material Design Start Date Picker--------------------------

//--------------------DATE PICKER FOR START TREATMENT-----------------------
        //START DATE
        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Start Date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setTheme(R.style.MaterialCalendarTheme)
                        .build();

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        formattedStartDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .format(new Date(selection));
                        System.out.println(">[InsrtDgn 05 onCreate] startDatePicker: " + formattedStartDate);

                    }
                });
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL DATE PICKER");
            }
        });
        System.out.println(">[InsrtDgn 05.1 onCreate]: start date= " + formattedStartDate);

        //--------------------Material Design End Date Picker--------------------------

//--------------------DATE PICKER FOR END TREATMENT-----------------------
        //END DATE
        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select End Date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setTheme(R.style.MaterialCalendarTheme)
                        .build();

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        formattedEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .format(new Date(selection));
                        System.out.println(">[InsrtDgn 06 onCreate] endDatePicker: " + formattedEndDate);
                    }
                });
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL DATE PICKER");
            }
        });
        System.out.println(">[InsrtDgn 06.1 onCreate]: start date= " + formattedEndDate);

        //Checked Check box to change editable attr
        enableFieldsChckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                treatmentEdt.setEnabled(isChecked);
                treatmentEdt.setBackground(ContextCompat.getDrawable(treatmentEdt.getContext(),R.drawable.rectangle_stroke_diagnosis));
                doseEdt.setEnabled(isChecked);
                doseEdt.setBackground(ContextCompat.getDrawable(doseEdt.getContext(),R.drawable.rectangle_stroke_diagnosis));
                //TODO: add enable and enabled color change in buttons

                if (!enableFieldsChckBox.isChecked()){
                    treatmentEdt.setEnabled(isChecked);
                    treatmentEdt.setBackground(ContextCompat.getDrawable(treatmentEdt.getContext(),R.drawable.rectangle_stroke_disabled));
                    doseEdt.setEnabled(isChecked);
                    doseEdt.setBackground(ContextCompat.getDrawable(doseEdt.getContext(),R.drawable.rectangle_stroke_disabled));
                }

            }
        });

        //DONE: Implement onClick of button: saveDiagn
        System.out.println(">[InsrtDgn 08 onCreate]: before saveDiagn onClick!!!");
//--------------------SAVE BUTTON ONCLICK IMPLEMENTATION-----------------------
        saveDiagn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DONE: Get content from editText-> diagnosisTxt, treeatmentTxt, doseTxt, infoTxt
//--------------------GETTING TEXTS FROM UI COMPONENTS-----------------------
                //TODO: check for empty fields => diagnosis, info/instructions: required fields, not null in API
                diagnosis = diagnosisEdt.getText().toString();
                treatment = treatmentEdt.getText().toString();
                dose = doseEdt.getText().toString();
                System.out.println(">[InsrtDgn 04 onCreate]: getting the content of edit text-> ");
                System.out.println(">[InsrtDgn 04.1 onCreate]: diagnosis= " + diagnosis + " treatment= " + treatment + " dose= " + dose);
                //DONE: Get content from editText-> infoTxt
                info = infoDiEdt.getText().toString();
                System.out.println(">[InsrtDgn 07 onCreate]: info= " + info);
                //DONE: Create Object Diagnosis
//--------------------CREATING A NEW OBJECT OF DIAGNOSIS-----------------------
//--------------------HERE THE DOCTOR_ID PARAMETER: GETS THE HOLE OBJECT INSTANCE-----------------------
                diagnosisReg = new Diagnosis(diagnosis,treatment,dose,formattedStartDate,formattedEndDate,info,doctorID,patientID);
                System.out.println("Diagnosis => " + diagnosisReg.toString());
                //DONE: Implement API POST Request
//--------------------CALLS THE CREATE DIAGNOSIS REQUEST METHOD-----------------------
                createDiagnosisRequest(diagnosisReg);

            }
        });

        //TODO: Implementation of onClick Listener for pdf extraction
    }

    private void createDiagnosisRequest(Diagnosis diagnosis) {
//--------------------ENTERS METHOD-----------------------
        RetrofitService retrofitService = new RetrofitService();
        DiagnosisApi diagnosisApi = retrofitService.getRetrofit().create(DiagnosisApi.class);
        System.out.println("//////////////inside create diagnosis + before enqueue/////////////");
//--------------------RUN STOPS HERE-----------------------
//--------------------THE ISSUE SEEMS TO BE WITH RETROFIT THAT CANNOT CREATE THE BODY FOR THE POST REQUEST--------------------
//--------------------ALSO GO TO DIAGNOSIS_API => FOR THE ISSUE WITH BODY CREATION-----------------------
//--------------------SECOND ISSUE IS WITH PARAMETERS IN DIAGNOSIS MISMATCH--------------------
//--------------------BUT AFTER EXTERNAL CONTROL I HAVEN'T DETECT SUCH ISSUE WITH FRONTEND AND BACKEND-----------------------
        diagnosisApi.createDiagnosis(diagnosis).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                System.out.println("//////////////inside onResponse/////////////");
                if (!response.isSuccessful() || response.body() == null){
                    System.out.println(">[InsrtDgn 01 createDiagnosisRequest]: unsuccessful response: " + response.isSuccessful() +
                            " || response == " + response.body() + "code = " + response.code());
                    Toast.makeText(InsertDiagnosisActivity.this, "Failed to create diagnosis", Toast.LENGTH_SHORT).show();
                }else {
                    System.out.println(">[InsrtDgn 02 createDiagnosisRequest]: response= " + response.isSuccessful() + " " + response.body());
                    Toast.makeText(InsertDiagnosisActivity.this, "Diagnosis Created", Toast.LENGTH_SHORT).show();
                }

                //DONE: intent to another screen
                Intent goToHomePage = new Intent(InsertDiagnosisActivity.this, DoctorHomePageActivity.class);
                startActivity(goToHomePage);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable throwable) {
                System.out.println(">[InsrtDgn 03] createDiagnosisRequest()-> onFailure: NOT OK");
                Toast.makeText(InsertDiagnosisActivity.this, "Failed to fetch request" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //--------------------COMMENTS FOR THODORIS - END-----------------------

    //API Get Request: search patient and having dynamic data in recycler view
//    private void searchPatient(String amka) {
//        //TODO: retrofit, diagnosis api, diagnosis (entity)
//        RetrofitService retrofitService = new RetrofitService();
//        DiagnosisApi diagnosisApi = retrofitService.getRetrofit().create(DiagnosisApi.class);
//        //TODO: API call for endpoint: searchPatByAmka -> get request
//        diagnosisApi.searchPatByAmka(amka).enqueue(new Callback<Patient>() {
//            @Override
//            public void onResponse(Call<Patient> call, Response<Patient> response) {
//                if (!response.isSuccessful() || response.body() == null){
//                    System.out.println(">[InsrtDgn 01] searchPatient()-> if for response " + response.body() + " : " + response.isSuccessful());
//                    Toast.makeText(InsertDiagnosisActivity.this, "No user Found", Toast.LENGTH_SHORT).show();
////                    recyclerView.setVisibility(View.GONE);
//                    return;
//                }
//
//                System.out.println(">[InsrtDgn 01.1] searchPatient()-> response.body(): " + response.body());
//                System.out.println(">[InsrtDgn 02] searchPatient()-> Assign response body to patient");
//                Patient tempPat = response.body();
//
//                if (tempPat == null){
//                    System.out.println(">[InsrtDgn 03] searchPatient()-> tempPat is NULL");
//                    return;
//                }
//
//                selectedPatient = Patient.getInstance();
//                System.out.println(">[InsrtDgn 04] searchPatient()-> matchedPatientInstance: " + selectedPatient);
//                if (selectedPatient == null){
//                    System.out.println(">[InsrtDgn 05] searchPatient() inside if-> matchedPatientInstance: " + selectedPatient);
//                    Toast.makeText(InsertDiagnosisActivity.this, "No user Found", Toast.LENGTH_SHORT).show();
////                    recyclerView.setVisibility(View.GONE);
//                    return;
//                }
//                //getting data from matched patient of response
//                selectedPatient.setPatientId(tempPat.getPatientId());
//                selectedPatient.setPatFirstName(tempPat.getPatFirstName());
//                selectedPatient.setPatLastName(tempPat.getPatLastName());
//                selectedPatient.setPatSecuredNum(tempPat.getPatSecuredNum());
//
//                //Setting data from matched patient to local class variables, for use in create diagnosis API request
//                patientID = selectedPatient.getPatientId();
//                fullNameP = concatFullName(selectedPatient.getPatFirstName(), selectedPatient.getPatLastName());
//                patAmka = selectedPatient.getPatSecuredNum();
//                System.out.println(">[InsrtDgn 05.1] searchPatient()-> matchedPatientInstance: " + patientID + " " + fullNameP + " " + patAmka);
//
//                System.out.println(">[InsrtDgn 06] searchPatient()-> matchedPatientInstance: " +
//                        selectedPatient.getPatientId() + " " + //this is what i need to send for the diagnosis creation
//                        selectedPatient.getPatFirstName() + " " +
//                        selectedPatient.getPatLastName() + " " +
//                        selectedPatient.getPatSecuredNum());
//                //TODO: add all these data to the list appeared in the recycler view
//                ArrayList<Patient> matchedPatList = new ArrayList<>();
//                matchedPatList.add(selectedPatient);
//                if (matchedPatList.isEmpty()){
//                    System.out.println(">[InsrtDgn 07] searchPatient() inside if-> matchedPatList: " + matchedPatList);
//                    Toast.makeText(InsertDiagnosisActivity.this, "matchedPatList = null", Toast.LENGTH_SHORT).show();
////                    recyclerView.setVisibility(View.GONE);
//                    return;
//                }
//
//                System.out.println(">[InsrtDgn 08] searchPatient()-> matchedPatList: " + matchedPatList);
////                recyclerView.setVisibility(View.VISIBLE);
//                System.out.println(">[InsrtDgn 09] searchPatient(): after recyclerView Visibility and before updatePatList() of [SrchRclrAdptr]");
////                searchRecyclerAdapter.updatePatList(matchedPatList);
//                System.out.println(">[InsrtDgn 10] searchPatient() -> after updateList() of adapter");
//            }
//
//            @Override
//            public void onFailure(Call<Patient> call, Throwable throwable) {
//                System.out.println(">[InsrtDgn 08] searchPatient()-> onFailure: NOT OK");
//                Toast.makeText(InsertDiagnosisActivity.this, "Failed to fetch request" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
////                recyclerView.setVisibility(View.GONE);
//            }
//        });
//    }

    //Called From searchPatient()
    //Creates the full name of the Patient showed in the recycler item
    private String concatFullName(String patFirstName, String patLastName) {
        return  patFirstName.concat(" ").concat(patLastName);
    }

}