package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.AppointmentsApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private Appointments appointments;
    private List<String> docFAndLName = new ArrayList<>();
    private ArrayList<Doctor> specialisedArray;
    private ArrayAdapter<String> doctorAdapter;
    private Integer idDoc;
    private Integer bookPat;
    private LocalDate localDateFrmt;
    private LocalTime localTimeFrmt;
    private BottomNavigationView bottomNavigationView;
//    private RetrofitService retrofitService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        //TODO : fields validation

        bottomNavigationView = findViewById(R.id.bottomNavBar);

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
                        localDateFrmt = LocalDate.parse(formattedDate, dateFormatter);
                    }
                });
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL DATE PICKER");
            }
        });
        System.out.println("BTN => Date Selected : " + formattedDate);

        //DONE : implement time picker
        //Adding Material Design Time Picker in Pick Time Button of xml file
        picktimeBtn.setOnClickListener(new View.OnClickListener() {
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
                        timeTxt.setText("Selected Time : " + formattedTime);
                        //Convert Date String to LocalDate
                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                        localTimeFrmt = LocalTime.parse(formattedTime, timeFormatter);
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
//                appointments = new Appointments(localDateFrmt,localTimeFrmt,comments,selectedDocId,bookPat);
                appointments = new Appointments(formattedDate,formattedTime,comments,selectedDocId,bookPat);
                System.out.println("Appointment data: " + appointments);
//                System.out.println("TXT => Comments Selected : " + comments);
                System.out.println("Specialisation => " + specialisation + " Doctor => " + doctor + " Date => " + formattedDate + " Time => " + formattedTime + " Commennts => " + comments);
                Log.d("TAG 2001", "onClick: " + "Specialisation => " + specialisation + " Doctor => " + doctor + " Date => " + formattedDate + " Time => " + formattedTime + " Commennts => " + comments);

                //DONE: Post request implementation
                System.out.println("[Appnmnt 1] Before createAppointment()");
                createAppointForPat(appointments);
            }

        });

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
                    Log.d("[APP 1] TAG:" + " SUCCESS: ", "onResponse: " + response.body());
                    Toast.makeText(BookAppointmentActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
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