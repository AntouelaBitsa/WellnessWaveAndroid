package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Diagnosis;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.AppointmentsApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.DocManageAppointmentsAdapter;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.LogInDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageAppointmentsActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private CalendarView calendarView;
    private DatePicker datePickerCalendar;
    private String formattedDate, selectedDate;

    private RecyclerView appointReclrView;
    private DocManageAppointmentsAdapter appointManageAdapter;
    private Dialog cancelPopUp;
    private Button popUpYesBtn, popUpNoBtn;

    private Doctor docInstance;
    private Patient patInstance;
    private Integer docID;
    private List<Appointments> appointmentsList;
    private LogInDTO userTypeInstance;
    private Integer userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_appointments);
        
        
        bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(getApplicationContext(), DoctorHomePageActivity.class));
                    finish();
                    return true;
                }else if (item.getItemId() == R.id.nav_manage_appointments) {
                    return true;
                }else if (item.getItemId() == R.id.nav_diagn_history){
                    startActivity(new Intent(getApplicationContext(), DiagnosisRecordsActivity.class));
//                    finish();
                    return true;
                }else if (item.getItemId() == R.id.nav_profile) {
                    startActivity(new Intent(getApplicationContext(), DocDetails.class));
                    finish();
                    return true;
                }else{
                    return false;
                }
            }
        });

        userTypeInstance = LogInDTO.getInstance();
        if (userTypeInstance == null){
            System.out.println("[Manage Appointments] userTypeInstance == NULL");
        }
        System.out.println("[Manage Appointments] userTypeInstance == " + userTypeInstance.getUserType());

        userType = userTypeInstance.getUserType();
        if (userType == 1){
            docInstance = Doctor.getInstance();
            System.out.println("TEST PRINT SOS 2 DOCTOR: "+ docInstance.getDocId());
            if (docInstance == null){
                System.out.println("[ManageAppoint 1] docInstance == null");
            }
            System.out.println("[ManageAppoint 2] docInstance.getDocUsername()= " + docInstance.getDocUsername());
            System.out.println("[ManageAppoint 3] docInstance.getUserType()= " + docInstance.getUserType());
        } else if (userType == 2) {
            patInstance = Patient.getInstance();
            System.out.println("TEST PRINT SOS 2 PATIENT: "+ patInstance.getPatientId());
            if (patInstance == null){
                System.out.println("[ManageAppoint 4] patInstance == null");
            }
            System.out.println("[ManageAppoint 5] patInstance.getPatUsername()= " + patInstance.getPatUsername());
            System.out.println("[ManageAppoint 6] patInstance.getUserType()= " + patInstance.getUserType());
        }

        appointmentsList = new ArrayList<>();
        appointReclrView = findViewById(R.id.manageRecycler);
        appointManageAdapter = new DocManageAppointmentsAdapter(appointmentsList, this, new DocManageAppointmentsAdapter.OnBtnListener() {
            @Override
            public void onCancelButtonClick(Appointments appoint) {
                callCancelPopUpDialog(appoint);
                cancelPopUp.show();
            }
        });
        appointReclrView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        appointReclrView.setAdapter(appointManageAdapter);

        calendarView = findViewById(R.id.calendarView);
        // Set a listener for date changes
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Month is 0-based, so add 1
//                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                selectedDate = String.format("%04d-%02d-%02d", year, month+1, dayOfMonth);
                // Display selected date
                Toast.makeText(ManageAppointmentsActivity.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
                if (userType == 1){
                    getDateBasedAppointmentsDoc(selectedDate, docInstance.getDocId(), userType);
                } else if (userType == 2) {
                    getDateBasedAppointmentsDoc(selectedDate, patInstance.getPatientId(), userType);
                }

            }
        });


    }

    private void callCancelPopUpDialog(Appointments appoint) {
        cancelPopUp = new Dialog(ManageAppointmentsActivity.this);
        cancelPopUp.setContentView(R.layout.cancel_appoint_popup_dialog);
        cancelPopUp.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cancelPopUp.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_popup_background));
        cancelPopUp.setCancelable(true);//when user clicks outside the dialog can not close

        popUpNoBtn = cancelPopUp.findViewById(R.id.rescheduleBtn);
        popUpYesBtn = cancelPopUp.findViewById(R.id.rschdlBtnPopUp);

        popUpYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DONE: business logic on cancellation for API Request
                Integer appointID = appoint.getAppointmentId();
//                deleteAppoint(appointID);
                cancelPopUp.dismiss();
            }
        });

        popUpNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelPopUp.dismiss();
            }
        });
    }

    private void getDateBasedAppointmentsDoc(String selectedDate, Integer userId, Integer userType) {
        //TODO: API call to endpoint
        System.out.println("Inside getDateBasedAppointments()");
        System.out.println("[SOS PRINT] Incoming Parameters: Id= "+ userId + " and SelectedDate= " + selectedDate + " and userType= " + userType);
        RetrofitService retrofitService = new RetrofitService();
        AppointmentsApi appointmentsApi = retrofitService.getRetrofit().create(AppointmentsApi.class);
        System.out.println("[SOS PRINT] --> before retrofit get request call");
        appointmentsApi.getAppointmentsOnDateSelected(selectedDate, userId, userType).enqueue(new Callback<List<Appointments>>() {
            @Override
            public void onResponse(Call<List<Appointments>> call, Response<List<Appointments>> response) {
                System.out.println("[ManageAppointmentsActivity] onResponse 1 : response BODY-> " + response.body());
                appointmentsList.clear();
                if (!response.isSuccessful() || response.body() == null) {
                    System.out.println("[ManageAppointmentsActivity] onResponse 2 : successful= no + body== null");
                    Toast.makeText(ManageAppointmentsActivity.this, "Failed to get doctor's appointments list", Toast.LENGTH_SHORT).show();
                    appointManageAdapter.notifyDataSetChanged();
                    return;
                }

//                if (response.body() == null){
//                    System.out.println("[ManageAppointmentsActivity]: response body is NULL" + response.body());
//                    return;
//                }
                System.out.println("[ManageAppointmentsActivity] onResponse 3 : response BODY 2--> " + response.body());
                appointmentsList.addAll(response.body());
                //patUsername.setText(pUsername);
                if (appointmentsList.isEmpty()) {
                    System.out.println("[ManageAppointmentsActivity] onResponse 4 : the appointmentsList is empty");
                }
                System.out.println("[ManageAppointmentsActivity] LIST SIZE IN RESPONSE: " + appointmentsList.size());
                appointManageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Appointments>> call, Throwable throwable) {
                System.out.println("[ManageAppointmentsActivity] onFailure: NOT OK");
                System.out.println("[ManageAppointmentsActivity] onFailure - throwable.getMessage() : " + throwable.getMessage());
                Toast.makeText(ManageAppointmentsActivity.this, "Failed to fetch request" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}