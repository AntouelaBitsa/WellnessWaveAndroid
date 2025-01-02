package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.AppointmentsApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RescheduleAppointment extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Patient patInstance;
    private Doctor docInstance;
    private Appointments rescheduledAppoint;
    private TextView fullNameTxt, amkaTxt;
    private TextInputEditText prefilledDate, prefilledTime, prefilledComments, rescheduleComment;
    private Button dateBtn, timeBtn, rescheduleBtn, cancelBtn;

    private String fullNameP, patAmka, rescheduledComm;
    private Integer patientID, doctorID;
    private LocalDate localDateFrmt;
    private LocalTime localTimeFrmt;
    private String formattedDate ="01-01-2000", formattedTime="00:00";
    private int hour=00, minute=00;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reschedule_appointment);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
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

        fullNameTxt = findViewById(R.id.patFullNameReTxt);
        amkaTxt = findViewById(R.id.patAmkaReTxt);
        prefilledComments = findViewById(R.id.prefilledAppointEdt);
        prefilledDate = findViewById(R.id.prefilledDateEdt);
        prefilledTime = findViewById(R.id.prefilledTimeEdt);
        dateBtn = findViewById(R.id.dateReBtn);
        timeBtn = findViewById(R.id.timeReBtn);
        rescheduleComment = findViewById(R.id.commentReEdt);
        rescheduleBtn = findViewById(R.id.rescheduleBtn);
        cancelBtn = findViewById(R.id.rschdlBtnPopUp);

        Intent getFromHopePage = getIntent();
        Appointments appointmentObj = (Appointments) getFromHopePage.getSerializableExtra("appoint");
        System.out.println("[Reschedule *1*] intent data: " + appointmentObj.toString());

        Patient currentPat = appointmentObj.getPatient();
        patInstance = Patient.getInstance();
        patInstance.setPatientData(currentPat);
        if (patInstance != null){
            patAmka = patInstance.getPatSecuredNum();
            System.out.println("Patient Amka= " + patAmka + "vs GETTING WITH INSTANCE= " + patInstance.getPatSecuredNum());
            fullNameP = patInstance.getPatFirstName().concat(" ").concat(patInstance.getPatLastName());
            patientID = patInstance.getPatientId();
        }else{
            System.out.println("Failed to fetch selected patient");
            return;
        }

        docInstance = Doctor.getInstance();
        if (docInstance != null){
            doctorID = docInstance.getDocId();
        }else{
            System.out.println("Failed to fetch connected doctor");
            return;
        }

        System.out.println("Insert Diagnosis: [TEST PRINT one]-> selectedPatient = " + patInstance.getPatientId() +" " + patInstance.getPatLastName()
                +" "+ patInstance.getPatSecuredNum());
        System.out.println("Insert Diagnosis: [TEST PRINT]-> fullName = " + fullNameP);

        //TODO: set text on full name & amka based on user type
        fullNameTxt.setText(fullNameP);
        amkaTxt.setText(patAmka);
        System.out.println("Insert Diagnosis: [TEST PRINT]-> amka = " + patAmka + " " + amkaTxt.getText());

        prefilledComments.setText(appointmentObj.getAppointmentId().toString().concat(" ").concat(appointmentObj.getAppointInfo()));
        prefilledDate.setText(appointmentObj.getDate());
        prefilledTime.setText(appointmentObj.getTime());
        //TODO: use singleton pattern for appointment in order to get easy data without API calls

        //Implementation of date and time button pickers
        //Adding Material Design Date Picker in Pick Date Button of xml file
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setTheme(R.style.MaterialCalendarTheme)
                        .build();
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .format(new Date(selection));
                        //Convert Date String to LocalDate - Not needed
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        localDateFrmt = LocalDate.parse(formattedDate, dateFormatter);
                    }
                });
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL DATE PICKER");
            }
        });
        System.out.println("BTN => Date Selected : " + formattedDate);

        //Adding Material Design Time Picker in Pick Time Button of xml file
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(9)
                        .setMinute(0)
                        .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                        .setTitleText("Select Time")
                        .setTheme(R.style.MaterialTimePickerTheme)
                        .build();
                materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hour = materialTimePicker.getHour();
                        minute = materialTimePicker.getMinute();
                        formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                        //Convert Date String to LocalDate
                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                        localTimeFrmt = LocalTime.parse(formattedTime, timeFormatter);
                    }
                });
                materialTimePicker.show(getSupportFragmentManager(), "MATERIAL TIME PICKER");
            }
        });
        System.out.println("BTN => Time Selected : " + formattedTime);


        rescheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rescheduledComm = String.valueOf(rescheduleComment.getText());
                rescheduledAppoint = new Appointments(formattedDate, formattedTime, rescheduledComm, doctorID, patientID);
                rescheduleAPICall(rescheduledAppoint);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBack = new Intent(RescheduleAppointment.this, DoctorHomePageActivity.class);
                startActivity(goBack);
            }
        });
    }

    private void rescheduleAPICall(Appointments appoint) {
        System.out.println("[Rschdl 1] Inside rescheduleAPICall() before retrofit initialization -> appointment = " + appoint);
        RetrofitService retrofitAppointSrvc = new RetrofitService();
        System.out.println("[Rschdl 1.2] API initialization = ");
        AppointmentsApi appointmentsApi = retrofitAppointSrvc.getRetrofit().create(AppointmentsApi.class);
        appointmentsApi.createAppointments(appoint).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                System.out.println("[Rschdl 1.3] Inside on Response: " + response.body() + " => " + response.isSuccessful());
                if (response.isSuccessful()){
                    Log.d("[APPOINT 1] TAG:" + " SUCCESS: ", "onResponse: " + response.body());
                    Toast.makeText(RescheduleAppointment.this, "You have rescheduled this appointment successfully", Toast.LENGTH_LONG).show();
                }else {
                    Log.d("[APPOINT 1] TAG:" + " FAIL: ", "onResponse: FAILED " + response.body() + " " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(RescheduleAppointment.this, "Request Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("[APPOINT 2] TAG: " + " onFailure: ", "The Message is : " + t.getMessage());
            }
        });
    }
}