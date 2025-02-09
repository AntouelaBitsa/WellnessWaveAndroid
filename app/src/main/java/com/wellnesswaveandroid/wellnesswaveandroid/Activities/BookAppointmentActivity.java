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
    private String formattedDate ="01-01-2000", formattedTime="00:00", specialisation="DEFAULT", doctor="NONE", comments="NONE";
    private int hour=00, minute=00;
    private Integer selectedDocId;
    private Appointments appointments, responseAppoint;
    private List<String> docFAndLName = new ArrayList<>();
    private ArrayList<Doctor> specialisedArray;
    private ArrayAdapter<String> doctorAdapter;
//    private Integer idDoc;
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


        //DONE : add contents to spinner specialisation
        System.out.println("Adding data to specialisation spinner");
        Log.d("TAG 1 ", "Adding data to specialisation spinner");
        //Adding data to specialisation spinner from xml file - Static Data
        String[] spec = getResources().getStringArray(R.array.specialisations_array);
        ArrayAdapter<String> specialisationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spec);
        specialisationAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item_layout);
        specialisationSpinner.setAdapter(specialisationAdapter);

        System.out.println("Implementation: on item selected Of spinner");
        Log.d("TAG 2 ", "Implementation: on item selected Of spinner");
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
        System.out.println("SPN => Specialisation Selected : " + specialisation);

        //DONE : add contents to spinner Doctor
        //Adding data to Doctor Spinner the first and last name in method updateDoctorSpinner()
        doctorAdapter = new ArrayAdapter<>(BookAppointmentActivity.this, android.R.layout.simple_spinner_item, docFAndLName);
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
                    }
                });
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL DATE PICKER");
            }
        });
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

                        getUnavailableTime(formattedDate, selectedDocId);
                        Log.d(TAG, "onClick() Map booked slot = [" + bookedSlot+ "]");
                        checkHoursAvailability(bookedSlot, formattedTime);
                        //TODO: make changes on UI of the time picker
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
                //DONE: Data gathering
                appointments = new Appointments(formattedDate,formattedTime,comments,selectedDocId,bookPat);
                System.out.println("Appointment data: " + appointments);
//                System.out.println("TXT => Comments Selected : " + comments);
                System.out.println("Specialisation => " + specialisation + " Doctor => " + doctor + " Date => " + formattedDate + " Time => " + formattedTime + " Commennts => " + comments);
                Log.d("TAG 2001", "onClick: " + "Specialisation => " + specialisation + " Doctor => " + doctor + " Date => " + formattedDate + " Time => " + formattedTime + " Commennts => " + comments);

                //DONE: Post request implementation
                System.out.println("[Appnmnt 1] Before createAppointment()");
//                responseAppoint = new Appointments();
                createAppointForPat(appointments);

            }

        });

    }


    /**
     * In this method is implemented a series of conditions, where the system is inspecting if the selected doctor has availability in
     * @param bookedSlot for the selected date and
     * @param formattedTime
     */
    private void checkHoursAvailability(Map<String, Set<String>> bookedSlot, String formattedTime) {
        Log.d(TAG, "checkHoursAvailability() called with: bookedSlot = [" + bookedSlot + "], formattedTime = [" + formattedTime + "]");
        boolean dateExists = bookedSlot.keySet().stream().anyMatch(dateKey -> dateKey.equals(formattedDate));
        boolean timeExists = bookedSlot.values().stream().anyMatch(timeSet -> timeSet.contains(formattedTime));
//        boolean timeExists = bookedSlot.get(formattedDate).contains(formattedTime);
//        boolean dateExists = bookedSlot.containsKey(formattedDate);
        if (bookedSlot.isEmpty()){
            /**there formatted date does not exist in booked slot => create the slot*/
            Log.d(TAG, "checkHoursAvailability() calling createNewSlotRequest() with: bookedSlot = [" + formattedDate + " " + formattedTime + " " + selectedDocId + "]");
            createNewSlotRequest(new BookedSlots(formattedDate, formattedTime, selectedDocId), selectedDocId);
        } else if (dateExists == true && timeExists == false){ //and date exists
            /**formattedTime does not exists inside the booked slot => add time in booked slot for this date*/
            Log.d(TAG, "checkHoursAvailability() calling updateExistingSlotRequest() with: parameters = [" + formattedDate + " " + formattedTime + " " + selectedDocId + "]");
            updateExistingSlotRequest(formattedTime, formattedDate, selectedDocId);
        }
        else if (dateExists == true && timeExists == true){
            /**formattedTime exists inside the booked slot => show Toast message: please select another time for the appointment*/
            Toast.makeText(this, "Please select another time for the appointment", Toast.LENGTH_SHORT).show();
        }
    }

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

    private void createNewSlotRequest(BookedSlots bookedSlots, Integer selectedDocId) {
        Log.d(TAG, "createNewSlotRequest() called with: bookedSlots = [" + bookedSlots + "], selectedDocId = [" + selectedDocId + "]");
        RetrofitService retrofitService = new RetrofitService();
        BookedSlotsApi bookedSlotsApi = retrofitService.getRetrofit().create(BookedSlotsApi.class);
        bookedSlotsApi.createSlot(bookedSlots, selectedDocId).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (!response.isSuccessful() || response.body() == null){
                    Toast.makeText(BookAppointmentActivity.this, "Response Body" + response.body(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "[onResponse: createNewSlotRequest()] inside if condition response is: " + response.body() + " " + response.code() );
                    return;
                }

                Log.d(TAG, "[onResponse(): createNewSlotRequest()] called with: call = [" + call + "], response = [" + response.body() + "]");
                Toast.makeText(BookAppointmentActivity.this, "Slot Booked successfully, Slot: " + bookedSlots, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(BookAppointmentActivity.this, "Request Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "[onFailure: createNewSlotRequest()] Throwable Message is : " + t.getMessage());
            }
        });
    }

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

    //TODO: Find a way to export this and other related methods to CustomTimeValidator Class
//    private boolean isTimeValid(LocalTime time) {
//        if (time.isBefore(startTime) || time.isAfter(endTime)){
//            return false;
//        }
//
//        /**Get Booked Slots for selected Date*/
//        Set<LocalTime> bookedSlots = bookedSlotsByDate.getOrDefault(selectedLocalDate, new HashSet<>());
//        if (bookedSlots.contains(time)){
//            return false;
//        }
//
//        LocalTime previousSlot = time.minusMinutes(30);
//        LocalTime nextSlot = time.minusMinutes(30);
//
//        if (bookedSlots.contains(previousSlot) || bookedSlots.contains(nextSlot)){
//            return false;
//        }
//        return true;
//    }

    /**Get request to find all booked slots in DB based on the date selected by the user*/
//    private void getBookedSlotsByDate(String date) {
//        RetrofitService retrofitService = new RetrofitService();
//        BookedSlotsApi bookedSlotsApi = retrofitService.getRetrofit().create(BookedSlotsApi.class);
//
//        bookedSlotsApi.getBookedSlotsByDate(date).enqueue(new Callback<List<BookedSlots>>() {
//            @Override
//            public void onResponse(Call<List<BookedSlots>> call, Response<List<BookedSlots>> response) {
//                if (!response.isSuccessful() && response.body() == null){
//                    System.out.println("[BookAppoint getBookedSlotsByDate()] (onResponse): successful = " + response.isSuccessful()
//                            + " body == " + response.body());
//                    Toast.makeText(BookAppointmentActivity.this, "Failed to get BookedSlots of selected Date", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                List<BookedSlots> slots = response.body();
//                System.out.println("[BookAppoint getBookedSlotsByDate() 01] (onResponse): slot = " + slots);
//                Set<LocalTime> bookedTimes = slots.stream().
//                        map(slot -> LocalTime.parse(slot.getSlotTime().toString())).collect(Collectors.toSet());
//                System.out.println("[BookAppoint getBookedSlotsByDate() 02] (onResponse): bookedTimes = " + bookedTimes);
//
//                bookedSlotsByDate.put(LocalDate.parse(date), bookedTimes);
//                System.out.println("[BookAppoint getBookedSlotsByDate() 03] (onResponse): bookedSlotsByDate = " + bookedSlotsByDate);
//            }
//
//            @Override
//            public void onFailure(Call<List<BookedSlots>> call, Throwable throwable) {
//                System.out.println("[BookAppoint getBookedSlotsByDate() 03] (onFailure): NOT OK");
//                System.out.println("[BookAppoint getBookedSlotsByDate() 03] (onFailure): throwable.getMessage() : " + throwable.getMessage());
//                Toast.makeText(BookAppointmentActivity.this, "Failed to fetch request" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    /**
     *
     * @return
     */
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
//                    String message = response.body().getMessage();
//                    if (message.isEmpty() || message == null){
//                        Toast.makeText(BookAppointmentActivity.this, "No appointments found", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    Gson gson = new Gson();
//                    Type appointment = new TypeToken<Appointments>(){}.getType();
//                    responseAppoint = gson.fromJson(message, appointment);
//                    Log.d("TAG 2003", "response Appointment");
                    /**Save Slot via API POST Request*/
                    Log.d(TAG, "onResponse: before createBookSlots() selectedLocalTime: " + selectedLocalTime + " selectedLocalDate: " + selectedLocalDate);

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

//    private void createBookSlot(LocalDate date, LocalTime time) {
//        Log.d(TAG, "createBookSlot: date: " + date + " | time: " + time);
//        BookedSlots bookedSlots = new BookedSlots(date.toString(), time.toString(),26);
//        Log.d(TAG, "createBookSlot: bookedSlots = " + bookedSlots);
//
//        RetrofitService retrofitService = new RetrofitService();
//        BookedSlotsApi bookedSlotsApi = retrofitService.getRetrofit().create(BookedSlotsApi.class);
//
//        bookedSlotsApi.createSlot(bookedSlots).enqueue(new Callback<Result>() {
//            @Override
//            public void onResponse(Call<Result> call, Response<Result> response) {
//                Log.d("TAG 2004", "onResponse: createBookSlot()");
//                if (!response.isSuccessful() && response.body() == null) {
//                    Log.d("API_NULL_RESPONSE", "Slot response is null!");
//                    return;
//                }
//                Log.d("API_SUCCESS", "Slot booked successfully | response.body(): " + response.body() );
//            }
//
//            @Override
//            public void onFailure(Call<Result> call, Throwable throwable) {
//                Log.e("API_ERROR", "Error saving booked slot", throwable);
//            }
//        });
//    }

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