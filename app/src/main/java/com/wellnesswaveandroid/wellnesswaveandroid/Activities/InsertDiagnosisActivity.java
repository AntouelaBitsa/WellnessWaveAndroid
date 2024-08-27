package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Diagnosis;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.DiagnosisApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.SearchRecyclerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertDiagnosisActivity extends AppCompatActivity {
    //Components
    private SearchView searchView;
    private MaterialAutoCompleteTextView diagnosisTxt, treatmentTxt, doseTxt, infoTxt;
    private Button startDateBtn, endDateBtn, saveDiagn;

    //Recycler View
    private RecyclerView recyclerView;
    private SearchRecyclerAdapter searchRecyclerAdapter;
    private ArrayList<Patient> patientList;
    private Patient selectedPatient;

    //Handling delayed API Request
    Handler handler = new Handler(Looper.getMainLooper());
    Runnable searchRunnable;

    //Data after the selection of user
    private Patient matchedPatient;
    private Integer patientID, doctorID;
    private String patAmka, fullNameP;

    //Date Picker variables
    private String formattedStartDate, formattedEndDate;

    //Diagnosis Object For Post Request
    private Diagnosis diagnosisReg;

    private Doctor docInstance;

    String diagnosis, treatment, dose, info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_diagnosis);

        //TODO: Search Bar-> search patient based on AMKA and select a patient
        System.out.println(">[InsrtDgn 01] onCreate : insert diagnosis HERE");

        //Components Initialization
        recyclerView = findViewById(R.id.patRecyclerView);
        searchView = findViewById(R.id.patSearchBar);
        diagnosisTxt = findViewById(R.id.diagnosisEdt);
        treatmentTxt = findViewById(R.id.treatmentEdt);
        doseTxt = findViewById(R.id.doseEdt);
        startDateBtn = findViewById(R.id.startDatePckrBtn);
        endDateBtn = findViewById(R.id.endDatePckrBtn);
        infoTxt = findViewById(R.id.infoEdt);
        saveDiagn = findViewById(R.id.saveDiagnBtn);

        //TODO: get connected doctors id
        docInstance = Doctor.getInstance();
        if (docInstance != null) {
            doctorID = docInstance.getDocId();
            System.out.println("////////////////doctorID => " + doctorID);
        }else{
            System.out.println("[SOS ID] docInstance == null");
        }

//        Intent intent2 = getIntent();
//        doctorID = Integer.valueOf(intent2.getStringExtra("doc_id"));
        System.out.println(">[InsrtDgn 01.1 onCreate]: doctorID= " + doctorID);

        //Search View Session
        searchView.clearFocus();
        System.out.println(">[InsrtDgn 02 onCreate] : before setOnQueryTextFocusChangeListener");
        //Classic way
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //When the user clicks the search bar change visibility of recycler view component on layout file
                if(hasFocus){
                    recyclerView.setVisibility(View.VISIBLE);
                }else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

        System.out.println(">[InsrtDgn 03 onCreate] : before setOnQueryTextListener");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println(">[InsrtDgn 04 onCreate] : inside onQueryTextChange");
//                fileList(newText); //for static data
                //DONE: Delayed API call
                System.out.println(">[InsrtDgn 05 onCreate] : before if -> searchRunnable != null ???");
                if (searchRunnable != null){
                    handler.removeCallbacks(searchRunnable);
                }
                System.out.println(">[InsrtDgn 06 onCreate] : before run() implementation");
                searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (!newText.isEmpty()){
                            //DONE: method to implement the API call
                            searchPatient(newText);  //-> newText is the amka that the user is typing into the search view
                        }else {
                            recyclerView.setVisibility(View.GONE);
                            searchRecyclerAdapter.updatePatList(new ArrayList<>());
                        }
                    }
                };
                handler.postDelayed(searchRunnable, 500);
                return false;
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        patientList = new ArrayList<>();

        // Initialize the adapter with the click listener
        searchRecyclerAdapter = new SearchRecyclerAdapter(this, patientList, new SearchRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Patient patient) {
                selectedPatient = patient;
                System.out.println(">[InsrtDgn 00] onItemClick() inside -> searchRecyclerAdapter: " + selectedPatient);
                Toast.makeText(InsertDiagnosisActivity.this, "Selected: " + patient.getPatFirstName(), Toast.LENGTH_SHORT).show();

                // Optionally, hide the RecyclerView after selecting an item
                recyclerView.setVisibility(View.GONE);
            }
        });
        recyclerView.setAdapter(searchRecyclerAdapter);

        //here where the edit text context set to string

        //TODO: Set Date Picker in buttons->  startDateBtn, endDateBtn
        //TODO: Get content from date selection button
        //--------------------Material Design Start Date Picker--------------------------
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

        //here where the edit text context set to string

        //TODO: Implement onClick of button: saveDiagn
        System.out.println(">[InsrtDgn 08 onCreate]: before saveDiagn onClick!!!");
        saveDiagn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Get content from editText-> diagnosisTxt, treeatmentTxt, doseTxt, infoTxt
                diagnosis = diagnosisTxt.getText().toString();
                treatment = treatmentTxt.getText().toString();
                dose = doseTxt.getText().toString();
                System.out.println(">[InsrtDgn 04 onCreate]: getting the content of edit text-> ");
                System.out.println(">[InsrtDgn 04.1 onCreate]: diagnosis= " + diagnosis + " treatment= " + treatment + " dose= " + dose);
                //TODO: Get content from editText-> infoTxt
                info = infoTxt.getText().toString();
                System.out.println(">[InsrtDgn 07 onCreate]: info= " + info);
                //TODO: Create Object Diagnosis
                diagnosisReg = new Diagnosis(diagnosis,treatment,dose,formattedStartDate,formattedEndDate,info,doctorID,patientID);
                System.out.println("Diagnosis => " + diagnosisReg.toString());
                //TODO: Implement API POST Request
                createDiagnosisRequest(diagnosisReg);
            }
        });
    }

    private void createDiagnosisRequest(Diagnosis diagnosis) {
        RetrofitService retrofitService = new RetrofitService();
        DiagnosisApi diagnosisApi = retrofitService.getRetrofit().create(DiagnosisApi.class);
        System.out.println("//////////////inside create diagnosis + before enqueue/////////////");
        diagnosisApi.createDiagnosis(diagnosis).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                System.out.println("//////////////inside onResponse/////////////");
                if (!response.isSuccessful() || response.body() == null){
                    System.out.println(">[InsrtDgn 01 createDiagnosisRequest]: unsuccessful response: " + response.isSuccessful() +
                            " || response == " + response.body());
                    Toast.makeText(InsertDiagnosisActivity.this, "Failed to create diagnosis", Toast.LENGTH_SHORT).show();
                }else {
                    System.out.println(">[InsrtDgn 02 createDiagnosisRequest]: response= " + response.isSuccessful() + " " + response.body());
                    Toast.makeText(InsertDiagnosisActivity.this, "Diagnosis Created", Toast.LENGTH_SHORT).show();
                }

                //TODO: intent to another screen ???
            }

            @Override
            public void onFailure(Call<Result> call, Throwable throwable) {
                System.out.println(">[InsrtDgn 03] createDiagnosisRequest()-> onFailure: NOT OK");
                Toast.makeText(InsertDiagnosisActivity.this, "Failed to fetch request" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                    System.out.println(">[InsrtDgn 01] searchPatient()-> if for response " + response.body() + " : " + response.isSuccessful());
                    Toast.makeText(InsertDiagnosisActivity.this, "No user Found", Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.GONE);
                    return;
                }

                System.out.println(">[InsrtDgn 01.1] searchPatient()-> response.body(): " + response.body());
                System.out.println(">[InsrtDgn 02] searchPatient()-> Assign response body to patient");
                Patient tempPat = response.body();

                if (tempPat == null){
                    System.out.println(">[InsrtDgn 03] searchPatient()-> tempPat is NULL");
                    return;
                }

                matchedPatient = Patient.getInstance();
                System.out.println(">[InsrtDgn 04] searchPatient()-> matchedPatientInstance: " + matchedPatient);
                if (matchedPatient == null){
                    System.out.println(">[InsrtDgn 05] searchPatient() inside if-> matchedPatientInstance: " + matchedPatient);
                    Toast.makeText(InsertDiagnosisActivity.this, "No user Found", Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.GONE);
                    return;
                }
                //getting data from matched patient of response
                matchedPatient.setPatientId(tempPat.getPatientId());
                matchedPatient.setPatFirstName(tempPat.getPatFirstName());
                matchedPatient.setPatLastName(tempPat.getPatLastName());
                matchedPatient.setPatSecuredNum(tempPat.getPatSecuredNum());

                //Setting data from matched patient to local class variables, for use in create diagnosis API request
                patientID = matchedPatient.getPatientId();
                fullNameP = concatFullName(matchedPatient.getPatFirstName(), matchedPatient.getPatLastName());
                patAmka = matchedPatient.getPatSecuredNum();
                System.out.println(">[InsrtDgn 05.1] searchPatient()-> matchedPatientInstance: " + patientID + " " + fullNameP + " " + patAmka);

                System.out.println(">[InsrtDgn 06] searchPatient()-> matchedPatientInstance: " +
                        matchedPatient.getPatientId() + " " + //this is what i need to send for the diagnosis creation
                        matchedPatient.getPatFirstName() + " " +
                        matchedPatient.getPatLastName() + " " +
                        matchedPatient.getPatSecuredNum());
                //TODO: add all these data to the list appeared in the recycler view
                ArrayList<Patient> matchedPatList = new ArrayList<>();
                matchedPatList.add(matchedPatient);
                if (matchedPatList.isEmpty()){
                    System.out.println(">[InsrtDgn 07] searchPatient() inside if-> matchedPatList: " + matchedPatList);
                    Toast.makeText(InsertDiagnosisActivity.this, "matchedPatList = null", Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.GONE);
                    return;
                }

                System.out.println(">[InsrtDgn 08] searchPatient()-> matchedPatList: " + matchedPatList);
                recyclerView.setVisibility(View.VISIBLE);
                System.out.println(">[InsrtDgn 09] searchPatient(): after recyclerView Visibility and before updatePatList() of [SrchRclrAdptr]");
                searchRecyclerAdapter.updatePatList(matchedPatList);
                System.out.println(">[InsrtDgn 10] searchPatient() -> after updateList() of adapter");
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable throwable) {
                System.out.println(">[InsrtDgn 08] searchPatient()-> onFailure: NOT OK");
                Toast.makeText(InsertDiagnosisActivity.this, "Failed to fetch request" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                recyclerView.setVisibility(View.GONE);
            }
        });


    }

    //Called From searchPatient()
    //Creates the full name of the Patient showed in the recycler item
    private String concatFullName(String patFirstName, String patLastName) {
        return  patFirstName.concat(" ").concat(patLastName);
    }

}