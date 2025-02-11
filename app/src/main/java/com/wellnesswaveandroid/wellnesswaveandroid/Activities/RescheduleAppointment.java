package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import static android.content.ContentValues.TAG;

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
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.BookedSlots;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.AppointmentsApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.BookedSlotsApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.CustomDateValidator;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.LogInDTO;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RescheduleAppointment extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Patient patInstance;
    private Doctor docInstance;
    private Appointments rescheduledAppoint, appointmentObj;
    private TextView fullNameTxt, amkaTxt, dateErrMsg, timeErrMsg;;
    private TextInputEditText prefilledDate, prefilledTime, prefilledComments, rescheduleComment;
    private Button dateBtn, timeBtn, rescheduleBtn, cancelBtn;

    private String fullNameP, patAmka, rescheduledComm;
    private Integer patientID, doctorID;
    private LocalDate localDateFrmt;
    private LocalTime localTimeFrmt;
    private String formattedDate = null, formattedTime = null;
    private int hour=00, minute=00;
    private Map<String, Set<String>> bookedSlot;

    /**
     * Compliant for both User's
     */
    private LogInDTO userTypeInstance;
    private Integer userType;
    private Class<?> targetHomePage, targetUserDetails;

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
                    startActivity(new Intent(getApplicationContext(), targetHomePage));
                    finish();
                    return true;
                }else if (item.getItemId() == R.id.nav_manage_appointments) {
                    //DONE: navigation manage appointments activity but for patient's data
                    startActivity(new Intent(getApplicationContext(), ManageAppointmentsActivity.class));
                    finish();
                    return true;
                }else if (item.getItemId() == R.id.nav_diagn_history) {
                    startActivity(new Intent(getApplicationContext(), DiagnosisRecordsActivity.class));
                    finish();
                    return true;
                }else if (item.getItemId() == R.id.nav_profile) {
                    startActivity(new Intent(getApplicationContext(), targetUserDetails));
                    finish();
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
        dateBtn = findViewById(R.id.startDateReBtn);
        timeBtn = findViewById(R.id.endDateReBtn);
        rescheduleComment = findViewById(R.id.commentReEdt);
        rescheduleBtn = findViewById(R.id.rescheduleBtn);
        cancelBtn = findViewById(R.id.rschdlBtnPopUp);

        dateErrMsg = findViewById(R.id.dateErrorTxtView);
        timeErrMsg = findViewById(R.id.timeErrorTxtView);

        Intent getFromHopePage = getIntent();
        appointmentObj = (Appointments) getFromHopePage.getSerializableExtra("appoint");
//        Patient patientObj = (Patient) getFromHopePage.getSerializableExtra("patient");
        System.out.println("[Reschedule *1*] intent data: " + appointmentObj.toString() +
                " \n & Patient: " + appointmentObj.getPatient().toString() +
                " \n & Doctor: " + appointmentObj.getDoctor().toString());

        if (appointmentObj == null){
            Toast.makeText(this, "Intent data for Doctor and Patient Passed a Null", Toast.LENGTH_SHORT).show();
            return;
        }

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
                Log.d("Doctor Logged In", "[RESCHEDULE] Doctor Instance NULL");
            }
            doctorID = docInstance.getDocId();

            //DONE: set text on full name & amka based on user type: Patient's Data
            String patfullName = appointmentObj.getPatient().getPatFirstName().concat(" ").concat(appointmentObj.getPatient().getPatLastName());
            fullNameTxt.setText(patfullName);
            patAmka = appointmentObj.getPatient().getPatSecuredNum();
            amkaTxt.setText("AMKA: " + patAmka);
            Log.d("Doctor Logged In", "[RESCHEDULE] User Type Prefilled Data: " + appointmentObj.getPatient().getUserType());


            targetHomePage = DoctorHomePageActivity.class;
            targetUserDetails = DocDetails.class;
            System.out.println("[Reschedule D] docInstance.getDocUsername()= " + docInstance.getDocUsername());
            System.out.println("[Reschedule D] docInstance.getUserType()= " + docInstance.getUserType());
        } else if (userType == 2) {
            patInstance = Patient.getInstance();
            System.out.println("TEST PRINT SOS 2 PATIENT: "+ patInstance.getPatientId());
            if (patInstance == null){
                Log.d("Patient Logged In", "[RESCHEDULE] Patient Instance NULL");
            }
            patAmka = patInstance.getPatSecuredNum();
            patientID = patInstance.getPatientId();

            if (doctorID == null){
                doctorID = appointmentObj.getDoctor().getDocId();
                Log.d("Patient Logged In", "Set doctor ID from appointmentObj (intent)");
            }

            TextView infoTitle = findViewById(R.id.personalInfoTxt);
            infoTitle.setText("Doctor Info");

            //DONE: set text on full name & amka based on user type: Doctor's Data
            String docfullName = appointmentObj.getDoctor().getDocFirstName().concat(" ").concat(appointmentObj.getDoctor().getDocLastName());
            fullNameTxt.setText("DR. " + docfullName);
            amkaTxt.setText("Ph. Number: " + appointmentObj.getDoctor().getDocPhoneNum());
            Log.d("Patient Logged In", "[RESCHEDULE] User Type Prefilled Data: " + appointmentObj.getDoctor().getUserType());

            targetHomePage = PatientHomePageActivity.class;
            targetUserDetails = PatDetails.class;
            System.out.println("[Reschedule P] patInstance.getPatUsername()= " + patInstance.getPatUsername());
            System.out.println("[Reschedule P] patInstance.getUserType()= " + patInstance.getUserType());
        }

        prefilledComments.setText(appointmentObj.getAppointmentId().toString().concat(" ").concat(appointmentObj.getAppointInfo()));
        prefilledDate.setText(appointmentObj.getDate());
        prefilledTime.setText(appointmentObj.getTime());

        //Implementation of date and time button pickers
        //Adding Material Design Date Picker in Pick Date Button of xml file
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setCalendarConstraints(createCalendarConstraints())
                        .setTheme(R.style.MaterialCalendarTheme)
                        .build();
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .format(new Date(selection));
                        dateBtn.setText(formattedDate);
                        //Convert Date String to LocalDate - Not needed
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        localDateFrmt = LocalDate.parse(formattedDate, dateFormatter);
                        System.out.println("Selected Date: " + formattedDate);

                        getUnavailableTime(formattedDate, doctorID);
                        Log.d("TAG 3000", "onClick() Map booked slot = [" + bookedSlot+ "]");
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
                        timeBtn.setText(formattedTime);
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

                if (formattedDate== null && formattedTime == null){
                    dateErrMsg.setVisibility(View.VISIBLE);
                    dateErrMsg.setText("Select Date");
                    dateErrMsg.setTextColor(dateErrMsg.getContext().getColor(R.color.errorred));

                    timeErrMsg.setVisibility(View.VISIBLE);
                    timeErrMsg.setText("Select Time");
                    timeErrMsg.setTextColor(dateErrMsg.getContext().getColor(R.color.errorred));
                    return;
                }

                rescheduledAppoint = new Appointments(formattedDate, formattedTime, rescheduledComm, doctorID, patientID, "rescheduled");
                checkHoursAvailability(formattedDate, formattedTime, doctorID);
//                updateCurrentAppointStatus(appointmentObj.getAppointmentId(), appointmentObj.setStatus("canceled"));
//                rescheduleAPICall(rescheduledAppoint);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBack = new Intent(RescheduleAppointment.this, targetHomePage);
                startActivity(goBack);
            }
        });
    }

    private void checkHoursAvailability(String incomingDate, String incomingTime, Integer doctorId) {
        if (bookedSlot == null){
            Toast.makeText(this, "Error fetching available Slots. Try Again.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (bookedSlot.containsKey(incomingDate)){
            Log.d("checkHoursAvailability(): ", "Date in bookedSlot Map " + bookedSlot.containsKey(incomingDate));
            Set<String> bookedTimes = bookedSlot.get(incomingDate);

            if (bookedTimes.contains(incomingTime)){
                Log.d("checkHoursAvailability(): ", "Time in bookedSlot Map " + bookedTimes.contains(incomingTime));
                Toast.makeText(this, "Selected time is unavailable. Choose another time.", Toast.LENGTH_SHORT).show();
                return;
            }else {
                Log.d("checkHoursAvailability(): ", "Time in bookedSlot Map " + bookedTimes.contains(incomingTime));
                BookedSlots newSlot = new BookedSlots(incomingDate, incomingTime, doctorId);
                Log.d("checkHoursAvailability(): ", "New bookedSlot Entry " + newSlot);
                createNewSlotRequest(newSlot);
            }
        }else {
            Log.d("checkHoursAvailability(): ", "Date in bookedSlot Map " + bookedSlot.containsKey(incomingDate));
            BookedSlots newSlot = new BookedSlots(incomingDate, incomingTime, doctorId);
            Log.d("checkHoursAvailability(): ", "New bookedSlot Entry " + newSlot);
            createNewSlotRequest(newSlot);
        }
        Log.d("checkHoursAvailability(): ", "Update Appointment Status to: " + appointmentObj.setStatus("canceled") + " with ID: " + appointmentObj.getAppointmentId());
        updateCurrentAppointStatus(appointmentObj.getAppointmentId(), appointmentObj.setStatus("canceled"));
        Log.d("checkHoursAvailability(): ", "New Appointment to be booked " + rescheduledAppoint);
        rescheduleAPICall(rescheduledAppoint);
    }

    //PUT Request
    private void updateExistingSlotRequest(String formattedTime, String formattedDate, Integer selectedDocId) {
        Log.d(TAG, "updateExistingSlotRequest() called with: formattedTime = [" + formattedTime + "], formattedDate = ["
                + formattedDate + "], selectedDocId = [" + selectedDocId + "]");
        RetrofitService retrofitService = new RetrofitService();
        BookedSlotsApi bookedSlotsApi = retrofitService.getRetrofit().create(BookedSlotsApi.class);
        bookedSlotsApi.updateSlot(formattedDate, selectedDocId, formattedTime).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (!response.isSuccessful() || response.body() == null){
                    Toast.makeText(RescheduleAppointment.this, "Response Body" + response.body(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "[onResponse: updateExistingSlotRequest()] inside if condition response is: " + response.body() + " " + response.code() );
                    return;
                }

                Log.d(TAG, "[onResponse(): updateExistingSlotRequest()] called with: call = [" + call + "], response = [" + response.body() + "]");
                Toast.makeText(RescheduleAppointment.this, "Slot Updated successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(RescheduleAppointment.this, "Request Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "[onFailure: updateExistingSlotRequest()] Throwable Message is : " + t.getMessage());
            }
        });
    }

    // POST REQUEST
    private void createNewSlotRequest(BookedSlots bookedSlots) {
        Log.d(TAG, "createNewSlotRequest() called with: bookedSlots = [" + bookedSlots + "], selectedDocId = [" + bookedSlots.getDoctor().getDocId() + "]");
        RetrofitService retrofitService = new RetrofitService();
        BookedSlotsApi bookedSlotsApi = retrofitService.getRetrofit().create(BookedSlotsApi.class);
        bookedSlotsApi.createSlot(bookedSlots).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (!response.isSuccessful() || response.body() == null){
                    Toast.makeText(RescheduleAppointment.this, "Response Body" + response.body(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "[onResponse: createNewSlotRequest()] inside if condition response is: " + response.body() + " " + response.code() );
                    return;
                }

                Log.d(TAG, "[onResponse(): createNewSlotRequest()] called with: call = [" + call + "], response = [" + response.body() + "]");
                Toast.makeText(RescheduleAppointment.this, "Slot Booked successfully.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(RescheduleAppointment.this, "Request Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "[onFailure: createNewSlotRequest()] Throwable Message is : " + t.getMessage());
            }
        });
    }

    /**
     * Get Request to API to get All booked slots based on Date selection
     * @param formattedDate is for the selected date from the user
     * @param selectedDocId is for the selected doctor from the user
     */
    private void getUnavailableTime(String formattedDate, Integer selectedDocId) {
        /**GET request on API*/
        Log.d(TAG, "getUnavailableTime() called with: formattedDate = [" + formattedDate + "], selectedDocId = [" + selectedDocId + "]");
//        LocalDate slotDate = LocalDate.parse(formattedDate);
        RetrofitService retrofitService = new RetrofitService();
        BookedSlotsApi bookedSlotsApi = retrofitService.getRetrofit().create(BookedSlotsApi.class);
        bookedSlotsApi.getBookedSlotsByDate(formattedDate, selectedDocId).enqueue(new Callback<Map<String, Set<String>>>() {
            @Override
            public void onResponse(Call<Map<String, Set<String>>> call, Response<Map<String, Set<String>>> response) {
                Log.d(TAG, "onResponse() called with: response.message = [" + response.message() + "], response.body = [" + response.body() + "]");
                if (!response.isSuccessful() || response.body() == null){
                    Toast.makeText(RescheduleAppointment.this, "Response Body" + response.body(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "[onResponse: getUnavailableTime()] inside if condition response is: " + response.body() + " " + response.code() );
                    return;
                }

                bookedSlot = response.body();
                System.out.println("[onResponse: getUnavailableTime()] bookedSlot = " + bookedSlot.toString());
                if (bookedSlot.isEmpty()){
                    Toast.makeText(RescheduleAppointment.this, "bookedSlot = " + bookedSlot.toString(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "[onResponse: getUnavailableTime()] inside if condition bookedSlot = " + bookedSlot.toString());
                    return;
                }
                Log.d(TAG, "[onResponse: getUnavailableTime()] SUCCESSFUL bookedSlot = " + bookedSlot.toString());
            }

            @Override
            public void onFailure(Call<Map<String, Set<String>>> call, Throwable t) {
                Toast.makeText(RescheduleAppointment.this, "Request Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "[onFailure: getUnavailableTime()] The Message is : " + t.getMessage());
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

                    Intent goBack = new Intent(RescheduleAppointment.this, targetHomePage);
                    startActivity(goBack);
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
                Toast.makeText(RescheduleAppointment.this, "Request Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("[APPOINT UPDATED] TAG: " + " onFailure: ", "The Message is : " + t.getMessage());
            }
        });
    }
}