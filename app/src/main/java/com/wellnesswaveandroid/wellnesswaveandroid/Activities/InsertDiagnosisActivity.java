package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.PDFGenerator;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertDiagnosisActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TextInputEditText diagnosisEdt, treatmentEdt, doseEdt, infoDiEdt;
    private Button startDateBtn, endDateBtn, saveDiagn;
    private ImageView extractToPDF;
    private TextView fullNamePatTxt, amkaPatTxt;
    private MaterialCheckBox enableFieldsChckBox;

    private Patient patientInstanceSlctd;
    private Doctor docInstance;
    private Diagnosis diagnosisObj;

    private Integer patientID, doctorID;
    private String patAmka, fullNameP;
    private String diagnosis, treatment, dose, info;

    //Date Picker variables
    private String formattedStartDate, formattedEndDate;

    //Diagnosis Object For Post Request
    private Diagnosis diagnosisReg;
    private PDFGenerator pdfGenerator;

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
                }else if (item.getItemId() == R.id.nav_manage_appointments) {
                    startActivity(new Intent(getApplicationContext(), ManageAppointmentsActivity.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.nav_diagn_history) {
                    startActivity(new Intent(getApplicationContext(), DiagnosisRecordsActivity.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.nav_profile) {
                    startActivity(new Intent(getApplicationContext(), DocDetails.class));
                    finish();
                    return true;
                } else {
                    return false;
                }
            }
        });

        //TODO: Search Bar-> search patient based on AMKA and select a patient
        System.out.println(">[InsrtDgn 01] onCreate : insert diagnosis HERE");


        diagnosisEdt = findViewById(R.id.prefilledAppointEdt);
        treatmentEdt = findViewById(R.id.treatementDiEdt);
        doseEdt = findViewById(R.id.doseDiEdt);
        startDateBtn = findViewById(R.id.dateStartBtn);
        endDateBtn = findViewById(R.id.dateEndBtn);
        infoDiEdt = findViewById(R.id.prefilledDateEdt);
        saveDiagn = findViewById(R.id.saveDiagnBtn);
        extractToPDF = findViewById(R.id.extractToPDFImg);
        fullNamePatTxt = findViewById(R.id.patFullNameReTxt);
        amkaPatTxt = findViewById(R.id.patAmkaReTxt);
        enableFieldsChckBox = findViewById(R.id.activateChckBx);


        //DONE: get connected doctors id
        docInstance = Doctor.getInstance();
        if (docInstance != null) {
            doctorID = docInstance.getDocId();
            System.out.println("////////////////doctorID => " + doctorID);
        }else{
            System.out.println("[SOS ID] docInstance == null");
        }

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
        saveDiagn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DONE: Get content from editText-> diagnosisTxt, treeatmentTxt, doseTxt, infoTxt
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
                diagnosisReg = new Diagnosis(diagnosis,treatment,dose,formattedStartDate,formattedEndDate,info,doctorID,patientID);
                System.out.println("Diagnosis => " + diagnosisReg.toString());
                //DONE: Implement API POST Request
                createDiagnosisRequest(diagnosisReg);

            }
        });



        //TODO: Implementation of onClick Listener for pdf extraction
        extractToPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: get request to get the created diagnosis
//                ActivityCompat.requestPermissions(InsertDiagnosisActivity.this,new String[]{
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
//
//                pdfGenerator = new PDFGenerator(InsertDiagnosisActivity.this);
//                pdfGenerator.generatePDF(patientInstanceSlctd, docInstance, diagnosis);
//                Toast.makeText(InsertDiagnosisActivity.this, "PDF File Generated Successfully", Toast.LENGTH_SHORT).show();
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

    //Called From searchPatient()
    //Creates the full name of the Patient showed in the recycler item
    private String concatFullName(String patFirstName, String patLastName) {
        return  patFirstName.concat(" ").concat(patLastName);
    }

}