package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.BookedSlots;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.AppointmentsApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.BookedSlotsApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.CustomDateValidator;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookAppointmentActivity extends AppCompatActivity {
    private Spinner specialisationSpinner, docSpecSpinner;
    private Button pickdateBtn, picktimeBtn, bookAppoint;
    private TextView dateTxt, timeTxt;
    private MaterialAutoCompleteTextView commentsMltlnTxt;
    private String formattedDate ="01-01-2000", formattedTime="00:00", specialisation="DEFAULT",
            doctor="NONE", comments="NONE", status="NOT DEFINED";
    private int hour=00, minute=00;
    private Integer selectedDocId;
    private Appointments appointments, responseAppoint;
    private List<String> docFAndLName = new ArrayList<>();
    private ArrayList<Doctor> specialisedArray;
    private ArrayAdapter<String> doctorAdapter;

    private Integer bookPat;
    private LocalDate selectedLocalDate;
    private LocalTime selectedLocalTime;
    private BottomNavigationView bottomNavigationView;

    /**Time Picker Dynamic dates available based on date selection: Variables*/
    private Map<String, Set<String>> bookedSlot = new HashMap<>();
    private final LocalTime startTime = LocalTime.of(9,0);
    private final LocalTime endTime = LocalTime.of(17,0);


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        //TODO : fields validation

        bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(getApplicationContext(), PatientHomePageActivity.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.nav_manage_appointments) {
                    startActivity(new Intent(getApplicationContext(), ManageAppointmentsActivity.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.nav_diagn_history) {
                    startActivity(new Intent(getApplicationContext(), DiagnosisRecordsActivity.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.nav_profile){
                    startActivity(new Intent(getApplicationContext(), PatDetails.class));
                    finish();
                    return true;
                }else {
                    return false;
                }
            }
        });

        System.out.println("Components initialisation");
        specialisationSpinner = (Spinner) findViewById(R.id.specialisationSpn);
        docSpecSpinner = findViewById(R.id.doctorSpn);
        pickdateBtn = findViewById(R.id.pickDateBtn);
        dateTxt = findViewById(R.id.slctdDateTxt);
        picktimeBtn = findViewById(R.id.pickTimeBtn);
        timeTxt = findViewById(R.id.slctdTimeTxt);
        commentsMltlnTxt = findViewById(R.id.commentsMltlnTxt);
        bookAppoint = findViewById(R.id.bookBtn);

        Intent getFromHomePage = getIntent();
        String source = getFromHomePage.getStringExtra("source");
        Log.d("SOS PRINT", "onCreate: Intent form POPUP DIALOG => source: " + source);
        Doctor doctorObj = (Doctor) getFromHomePage.getSerializableExtra("doc");
        Log.d("SOS PRINT", "onCreate: Intent form POPUP DIALOG => doctor: " + doctorObj);

        if (doctorObj != null){
            specialisation = doctorObj.getDocProfession();
            //DONE: set to docFAndLName the doctor's first and last name
            doctor = doctorObj.getDocFirstName().concat(" ").concat(doctorObj.getDocLastName());
            docFAndLName.add(doctor);
        }



        //DONE : add contents to spinner specialisation
        System.out.println("Adding data to specialisation spinner");
        Log.d("TAG 1 ", "Adding data to specialisation spinner");
        //Adding data to specialisation spinner from xml file - Static Data
        String[] spec = getResources().getStringArray(R.array.specialisations_array);
        ArrayAdapter<String> specialisationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spec);
        specialisationAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item_layout);
        specialisationSpinner.setAdapter(specialisationAdapter);

        //DONE : add contents to spinner Doctor
        //Adding data to Doctor Spinner the first and last name in method updateDoctorSpinner()
        doctorAdapter = new ArrayAdapter<>(BookAppointmentActivity.this, android.R.layout.simple_spinner_item, docFAndLName);
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        docSpecSpinner.setAdapter(doctorAdapter);

        /**
         * Condition to check source data
         * If navigation coming from PopUp Dialog (Specialization Carousel) set data from Intent
         * If navigation coming from Add New Appointment Card (Appointments Carousel) set data by implementing get Request on API
         */
        if (source.equals("popup")){
            Integer posSpec = specialisationAdapter.getPosition(specialisation);
            if (posSpec == -1){ return; }
            specialisationSpinner.setSelection(posSpec);
            Log.d("POPUP DATA", "[PopUp Incoming Data] Specialization: " + specialisation + " pos: " + posSpec);

            Integer posDoc = docFAndLName.indexOf(doctor);
            if (posSpec == -1){ return; }
            docSpecSpinner.setSelection(posDoc);
            selectedDocId = doctorObj.getDocId();
            Log.d("POPUP DATA", "[PopUp Incoming Data] Doctor: " + doctor + " pos: " + posDoc);
        }else{
            System.out.println("Implementation: on item selected Of spinner");
            Log.d("TAG 2 ", "Implementation: on item selected Of spinner");
            /**
             * Get Specialization Selection from Drop Down
             */
            specialisationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    Log.d("TAG 3 ", "Inside on item selected method");
                    specialisation = spec[position];
                    //DONE : POST request to get a list o doctors filtered by specialisation
                    //DONE : the endpoint need to get as parameters the specialisation String
//                getFilteredDocBySpecialisation(specialisation);
                    System.out.println("specialisation onClick => " + specialisation);
                    System.out.println("docFAndLName => " + docFAndLName.toString());
                    testGetSpec(specialisation);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });

            /**
             * Get Doctor Selection
             */
            docSpecSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    doctor = doctorAdapter.getItem(position);
                    System.out.println("[BookAppoint DEBUG] Selected Position: " + position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });

        }
        System.out.println("SPN => Specialisation Selected : " + specialisation);
        System.out.println("SPN => Doctor Selected : " + doctor);


        //DONE : implement date picker
        //Adding Material Design Date Picker in Pick Date Button of xml file
        pickdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setCalendarConstraints(createCalendarConstraints()) /**method to add validator to DatePicker*/
                        .setTheme(R.style.MaterialCalendarTheme)
                        .build();
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .format(new Date(selection));
                        dateTxt.setText("Selected Date : " + formattedDate);  //TEST
                        //Convert Date String to LocalDate - Not needed
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        selectedLocalDate = LocalDate.parse(formattedDate, dateFormatter);

//                        /**Get request to find all booked slots in DB based on the date selected by the user*/
//                        getBookedSlotsByDate(selectedLocalDate.toString());
                        getUnavailableTime(formattedDate, selectedDocId);
                        Log.d("TAG 3000", "onClick() Map booked slot = [" + bookedSlot+ "]");
                    }
                });
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL DATE PICKER");
            }
        });

        if (formattedDate == null){
            dateTxt.setText("Please select date.");
            return;
        }
        System.out.println("BTN => Date Selected : " + formattedDate);

        //DONE : implement time picker
        //Adding Material Design Time Picker in Pick Time Button of xml file
        //https://chatgpt.com/c/6788c168-6368-8006-ba9a-a808ed730a19 for time picker implementation
        picktimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                /**VALIDATION RULE*/
//                if (selectedLocalDate == null){
//                    timeTxt.setText("Please Select Date First!");
//                    return;
//                }

                MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(startTime.getHour())
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
                        timeTxt.setText("Selected Time : " + formattedTime);
                        //Convert Date String to LocalDate
                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                        selectedLocalTime = LocalTime.parse(formattedTime, timeFormatter);
                        Log.d("TAG 2002", "selectedLocalTime: " + selectedLocalTime + " selectedLocalDate: " + selectedLocalDate);

//                        /**Save Booked Slot after the appointment is booked*/
//                        /**VALIDATION RULE*/
//                        if (!isTimeValid(selectedLocalTime)){
//                            System.out.println();
//                            timeTxt.setText("Invalid Time: " + selectedLocalTime);
//                        }else {
//                            timeTxt.setText("Selected Time: " + selectedLocalTime);
//
//                        }
                    }
                });
                materialTimePicker.show(getSupportFragmentManager(), "MATERIAL TIME PICKER");
            }
        });
        System.out.println("BTN => Time Selected : " + formattedTime);

        //DONE: OnClick listener bookAppointment
        bookAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Patient p = Patient.getInstance();
                if (p == null){
                    System.out.println("[P] User desn't have ID");
                }
                //DONE: here we get the data we need from instance of patient
                bookPat = p.getPatientId();
                System.out.println("[P2] patientID = " + bookPat + " [D2] doctorID = " + selectedDocId);
                //DONE: get the text from multiline edit text
                comments = String.valueOf(commentsMltlnTxt.getText());
                status = "active";
                //DONE: Data gathering
                appointments = new Appointments(formattedDate,formattedTime,comments,selectedDocId,bookPat,status);
                System.out.println("Appointment data: " + appointments);
//                System.out.println("TXT => Comments Selected : " + comments);
                System.out.println("Specialisation => " + specialisation + " Doctor => " + doctor + " Date => " + formattedDate + " Time => " + formattedTime + " Commennts => " + comments);
                Log.d("TAG 2001", "onClick: " + "Specialisation => " + specialisation + " Doctor => " + doctor + " Date => " + formattedDate + " Time => " + formattedTime + " Commennts => " + comments);

                //DONE: Post request implementation
                System.out.println("[Appnmnt 1] Before createAppointment()");
//                responseAppoint = new Appointments();
                checkHoursAvailability(formattedDate, formattedTime, selectedDocId);
//                createAppointForPat(appointments);
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
        Log.d("checkHoursAvailability(): ", "New Appointment to be booked " + appointments);
        createAppointForPat(appointments);
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
                    Toast.makeText(BookAppointmentActivity.this, "Response Body" + response.body(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "[onResponse: updateExistingSlotRequest()] inside if condition response is: " + response.body() + " " + response.code() );
                    return;
                }

                Log.d(TAG, "[onResponse(): updateExistingSlotRequest()] called with: call = [" + call + "], response = [" + response.body() + "]");
                Toast.makeText(BookAppointmentActivity.this, "Slot Updated successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(BookAppointmentActivity.this, "Request Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "[onFailure: updateExistingSlotRequest()] Throwable Message is : " + t.getMessage());
            }
        });
    }

    // POST REQUEST
    private void createNewSlotRequest(BookedSlots bookedSlots) {
        Log.d(TAG, "createNewSlotRequest() called with: bookedSlots = [" + bookedSlots + "], selectedDocId = [" + selectedDocId + "]");
        RetrofitService retrofitService = new RetrofitService();
        BookedSlotsApi bookedSlotsApi = retrofitService.getRetrofit().create(BookedSlotsApi.class);
        bookedSlotsApi.createSlot(bookedSlots).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (!response.isSuccessful() || response.body() == null){
                    Toast.makeText(BookAppointmentActivity.this, "Response Body" + response.body(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "[onResponse: createNewSlotRequest()] inside if condition response is: " + response.body() + " " + response.code() );
                    return;
                }

                Log.d(TAG, "[onResponse(): createNewSlotRequest()] called with: call = [" + call + "], response = [" + response.body() + "]");
                Toast.makeText(BookAppointmentActivity.this, "Slot Booked successfully.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(BookAppointmentActivity.this, "Request Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(BookAppointmentActivity.this, "Response Body" + response.body(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "[onResponse: getUnavailableTime()] inside if condition response is: " + response.body() + " " + response.code() );
                    return;
                }

                bookedSlot = response.body();
                System.out.println("[onResponse: getUnavailableTime()] bookedSlot = " + bookedSlot.toString());
                if (bookedSlot.isEmpty()){
                    Toast.makeText(BookAppointmentActivity.this, "bookedSlot = " + bookedSlot.toString(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "[onResponse: getUnavailableTime()] inside if condition bookedSlot = " + bookedSlot.toString());
                    return;
                }
                Log.d(TAG, "[onResponse: getUnavailableTime()] SUCCESSFUL bookedSlot = " + bookedSlot.toString());
            }

            @Override
            public void onFailure(Call<Map<String, Set<String>>> call, Throwable t) {
                Toast.makeText(BookAppointmentActivity.this, "Request Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
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

    private void createAppointForPat(Appointments ap) {
        System.out.println("[Appnmnt 2] Inside createAppointment() before retrofit initialization -> appointment = " + ap);
        RetrofitService retrofitAppointSrvc = new RetrofitService();
        System.out.println("[Appnmnt 2.1] API initialization = ");
        AppointmentsApi appointmentsApi = retrofitAppointSrvc.getRetrofit().create(AppointmentsApi.class);
        appointmentsApi.createAppointments(ap).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                System.out.println("[Appnmnt 3] Inside on Response: " + response.body() + " => " + response.isSuccessful());
                if (response.isSuccessful()){
                    Log.d("[Appnmnt 3]" + " SUCCESS: ", "onResponse: " + response.body());
                    Toast.makeText(BookAppointmentActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    /**Save Slot via API POST Request*/
                    Log.d(TAG, "onResponse: before createBookSlots() selectedLocalTime: " + selectedLocalTime + " selectedLocalDate: " + selectedLocalDate);
                    Intent goToHomePage = new Intent(BookAppointmentActivity.this, PatientHomePageActivity.class);
                    startActivity(goToHomePage);
                }else {
                    Log.d("[APP 1] TAG:" + " FAIL: ", "onResponse: FAILED " + response.body() + " " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(BookAppointmentActivity.this, "Request Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("[APP 2] TAG: " + " onFailure: ", "The Message is : " + t.getMessage());
            }
        });
    }


    private void testGetSpec(String specialisation) {
        System.out.println("Specialisation = " + specialisation);
        System.out.println("OK1.1");

        RetrofitService retrofitService = new RetrofitService();
        AppointmentsApi api = retrofitService.getRetrofit().create(AppointmentsApi.class);

        System.out.println("OK1.2");
        api.getSpecialisedDoc(specialisation).enqueue(new Callback<Result>() {
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
                    Toast.makeText(BookAppointmentActivity.this, "Specialized Doc Doesn't Exist", Toast.LENGTH_LONG).show();
                    return;
                }

                if (message.isEmpty() || message == null){
                    Toast.makeText(BookAppointmentActivity.this, "No doctors found for the selected specialization", Toast.LENGTH_LONG).show();
                    return;
                }


                Gson gson = new Gson();
                Type doctorListType = new TypeToken<ArrayList<Doctor>>(){}.getType();
                specialisedArray = gson.fromJson(message, doctorListType);
                docFAndLName.clear();
                //Test print
                for (Doctor doc : specialisedArray) {
                    System.out.println("Doctor ID: " + doc.getDocId() + ", Name: " + doc.getDocFirstName() + " " + doc.getDocLastName());
                    selectedDocId = doc.getDocId();
                    docFAndLName.add(doc.getDocFirstName() + " " + doc.getDocLastName());
                }
                //TEST PRINTS
                System.out.println("[D2] ID : " + selectedDocId);
                System.out.println("[D2] docFAndLName : " + docFAndLName);
                doctorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                System.out.println("onFailure: NOT OK");
                Toast.makeText(BookAppointmentActivity.this, "Request Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("onFailure", "onResponse: " + t.getMessage());
            }
        });
    }

    /*private void updateDoctorSpinner() {
        runOnUiThread(() -> {
            ArrayAdapter<String> doctorAdapter = new ArrayAdapter<>(BookAppointmentActivity.this, android.R.layout.simple_spinner_item, docFAndLName);
            doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            docSpecSpinner.setAdapter(doctorAdapter);

            docSpecSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        doctor = doctorAdapter.getItem(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
            System.out.println("SPN => Doctor Selected : " + doctor);
        });
    }*/

}