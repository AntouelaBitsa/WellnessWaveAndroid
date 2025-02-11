package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Diagnosis;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.AppointmentsApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.DiagnosisApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.CustomDateValidator;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.FieldsValidators;
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
    private TextView fullNamePatTxt, amkaPatTxt, treatmentErrMsg, doseErrMsg, startDateErrMsg, endDateErrMsg, diagnosisErrMsg, infoErrMsg;
    private MaterialCheckBox enableFieldsChckBox;

    private Patient patientInstanceSlctd;
    private Doctor docInstance;
    private Diagnosis diagnosisObj;

    private Integer patientID, doctorID;
    private String patAmka, fullNameP;
    private String diagnosis, treatment, dose, info;

    //Date Picker variables
    private String formattedStartDate = null, formattedEndDate = null;

    //Diagnosis Object For Post Request
    private Diagnosis diagnosisReg;
    private PDFGenerator pdfGenerator;
    private FieldsValidators fieldsValidators;

    @SuppressLint("MissingInflatedId")
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
        System.out.println(">[InsrtDgn 01] onCreate : insert diagnosis HERE");

        diagnosisEdt = findViewById(R.id.diagnosisDiEdt);
        treatmentEdt = findViewById(R.id.treatementDiEdt);
        doseEdt = findViewById(R.id.doseDiEdt);
        startDateBtn = findViewById(R.id.dateStartBtn);
        endDateBtn = findViewById(R.id.dateEndBtn);
        infoDiEdt = findViewById(R.id.infoDiEdt);
        saveDiagn = findViewById(R.id.saveDiagnBtn);
        fullNamePatTxt = findViewById(R.id.patFullNameReTxt);
        amkaPatTxt = findViewById(R.id.patAmkaReTxt);
        enableFieldsChckBox = findViewById(R.id.activateChckBx);

        diagnosisErrMsg = findViewById(R.id.diagnosisErrorTxtView);
        infoErrMsg = findViewById(R.id.infoErrorTxtView);
        treatmentErrMsg = findViewById(R.id.treatmentErrorTxtView);
        doseErrMsg = findViewById(R.id.doseErrorTxtView);
        startDateErrMsg = findViewById(R.id.startDateErrorTxtView);
        endDateErrMsg = findViewById(R.id.endDateErrorTxtView);


        //DONE: get connected doctors id
        docInstance = Doctor.getInstance();
        if (docInstance != null) {
            doctorID = docInstance.getDocId();
            System.out.println("////////////////doctorID => " + doctorID);
        }else{
            System.out.println("[SOS ID] docInstance == null");
        }

        Intent getFromHopePage = getIntent();
        Appointments appointmentObj = (Appointments) getFromHopePage.getSerializableExtra("appoint");
        Patient patientObj = (Patient) getFromHopePage.getSerializableExtra("patient");
        System.out.println("[Reschedule *1*] intent data: appointmentID" + appointmentObj.getAppointmentId());

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


        //DONE: Set Date Picker in buttons->  startDateBtn, endDateBtn
        //DONE: Get content from date selection button
        //START DATE
        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Start Date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setCalendarConstraints(createCalendarConstraints())
                        .setTheme(R.style.MaterialCalendarTheme)
                        .build();

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        formattedStartDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .format(new Date(selection));
                        startDateBtn.setText(formattedStartDate);
                        System.out.println(">[InsrtDgn 05 onCreate] startDatePicker: " + formattedStartDate);

                    }
                });
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL DATE PICKER");
            }
        });
        System.out.println(">[InsrtDgn 05.1 onCreate]: start date= " + formattedStartDate);

        //END DATE PICKER
        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select End Date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setCalendarConstraints(createCalendarConstraints())
                        .setTheme(R.style.MaterialCalendarTheme)
                        .build();

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        formattedEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .format(new Date(selection));
                        endDateBtn.setText(formattedEndDate);
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
                //DONE: add enable and enabled color change in buttons

                if (!enableFieldsChckBox.isChecked()){
                    treatmentEdt.setEnabled(isChecked);
                    treatmentEdt.setBackground(ContextCompat.getDrawable(treatmentEdt.getContext(),R.drawable.rectangle_stroke_disabled));
                    doseEdt.setEnabled(isChecked);
                    doseEdt.setBackground(ContextCompat.getDrawable(doseEdt.getContext(),R.drawable.rectangle_stroke_disabled));
                }

            }
        });

        fieldsValidators = new FieldsValidators();

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

                /**
                 * Validation
                 */
                String diagnosisErrMessage = fieldsValidators.validateEmpty(diagnosisEdt.getText().toString().trim());
                diagnosisErrMsg.setVisibility(View.VISIBLE);
                diagnosisErrMsg.setText(diagnosisErrMessage);
                diagnosisErrMsg.setTextColor(diagnosisErrMsg.getContext().getColor(R.color.errorred));

                String infoErrMessage = fieldsValidators.validateEmpty(infoDiEdt.getText().toString().trim());
                infoErrMsg.setVisibility(View.VISIBLE);
                infoErrMsg.setText(infoErrMessage);
                infoErrMsg.setTextColor(infoErrMsg.getContext().getColor(R.color.errorred));

                boolean emptyTreatment = false;
                if (enableFieldsChckBox.isChecked()){
                    String treatmentErrMessage = fieldsValidators.validateEmpty(treatmentErrMsg.getText().toString().trim());
                    treatmentErrMsg.setVisibility(View.VISIBLE);
                    treatmentErrMsg.setText(treatmentErrMessage);
                    treatmentErrMsg.setTextColor(treatmentErrMsg.getContext().getColor(R.color.errorred));

                    String doseErrMessage = fieldsValidators.validateEmpty(doseEdt.getText().toString().trim());
                    doseErrMsg.setVisibility(View.VISIBLE);
                    doseErrMsg.setText(doseErrMessage);
                    doseErrMsg.setTextColor(doseErrMsg.getContext().getColor(R.color.errorred));

                    emptyTreatment = fieldsValidators.validateEmptyTreatment(treatmentEdt.getText().toString().trim(),
                            doseEdt.getText().toString().trim());
                }

                boolean emptyDiagnosis = fieldsValidators.validateEmptyDiagnosis(diagnosisEdt.getText().toString().trim(),
                        infoDiEdt.getText().toString().trim());

                if (emptyDiagnosis){
                    Toast.makeText(InsertDiagnosisActivity.this, "Please fill in the empty fields.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (enableFieldsChckBox.isChecked() && emptyTreatment) {
                    Toast.makeText(InsertDiagnosisActivity.this, "Please fill in the empty fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (formattedStartDate == null && formattedEndDate == null){
                    startDateErrMsg.setVisibility(View.VISIBLE);
                    startDateErrMsg.setText("Select Date");
                    startDateErrMsg.setTextColor(startDateErrMsg.getContext().getColor(R.color.errorred));

                    endDateErrMsg.setVisibility(View.VISIBLE);
                    endDateErrMsg.setText("Select Time");
                    endDateErrMsg.setTextColor(endDateErrMsg.getContext().getColor(R.color.errorred));
                    return;
                }

                //DONE: Implement API POST Request
                updateCurrentAppointStatus(appointmentObj.getAppointmentId(), appointmentObj.setStatus("diagnosed"));
                createDiagnosisRequest(diagnosisReg);

            }
        });

    }

    private CalendarConstraints createCalendarConstraints() {
        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();

        /**Disable Past Dates*/
        constraintBuilder.setStart(MaterialDatePicker.todayInUtcMilliseconds());

        /**Set custom validator for:
         * 1: Saturday and Sunday
         * 2: Holiday Dates
         * */
        constraintBuilder.setValidator(new CustomDateValidator());

        return constraintBuilder.build();
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
                Toast.makeText(InsertDiagnosisActivity.this, "Request Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("[APPOINT UPDATED] TAG: " + " onFailure: ", "The Message is : " + t.getMessage());
            }
        });
    }

    //Called From searchPatient()
    //Creates the full name of the Patient showed in the recycler item
    private String concatFullName(String patFirstName, String patLastName) {
        return  patFirstName.concat(" ").concat(patLastName);
    }

}