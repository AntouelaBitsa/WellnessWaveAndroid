package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.PatientApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatDetails extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private TextInputEditText passwordTxt, emailTxt, amkaTxt, phoneNumTxt, dobTxt;
    private TextView fullNameTxt, lastNameTxt, usernameTxt;
//    private RetrofitService retrofitService;
    private ImageView deleteAccount;
    private Button editPatProfileBtn;
    private Patient patInstance;
    private boolean btnState = false;
    private Map<String, Object> updatedFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pat_details);

        bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(getApplicationContext(), PatientHomePageActivity.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.nav_manage_appointments) {
                    startActivity(new Intent(getApplicationContext(), BookAppointmentActivity.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.nav_diagn_history) {
                    startActivity(new Intent(getApplicationContext(), DiagnosisRecordsActivity.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.nav_profile){
                    return true;
                }else {
                    return false;
                }
            }
        });

        //initialization: find views by id for edit texts
        fullNameTxt = findViewById(R.id.patFullNameTxt);
//        lastNameTxt = findViewById(R.id.patLNameTxt);
        usernameTxt = findViewById(R.id.patUsernameTxt);
//        passwordTxt = findViewById(R.id.patPasswordTxt);
        emailTxt = findViewById(R.id.patEmailTxt);
        amkaTxt = findViewById(R.id.patAmkaTxt);
        phoneNumTxt = findViewById(R.id.patPhoneNumTxt);
        dobTxt = findViewById(R.id.patDobTxt);
        editPatProfileBtn = findViewById(R.id.editPatProfileBtn);
        deleteAccount = findViewById(R.id.deletePatAccount);

        Log.d("[P] Are HEre", " Yes");
        Toast.makeText(PatDetails.this, "Intent worked Successfully", Toast.LENGTH_LONG).show();

        Intent importPatData = getIntent();
        String dfirstName = importPatData.getStringExtra("first_namep");
//        String lastName = importPatData.getStringExtra("last_namep");
        String dusername = importPatData.getStringExtra("usernamep");
        String demail = importPatData.getStringExtra("emailp");
        String dphoneNum = importPatData.getStringExtra("phonep");
        String ddob = importPatData.getStringExtra("dobp");

        patInstance = Patient.getInstance();
        if (patInstance == null){
            System.out.println("The doctor instance in DOCDETAILS Class is null");
            return;
        }

        System.out.println("Intent Print : " + dfirstName+ " "+dusername+
                " " +demail+ " " +dphoneNum+ " " +ddob);

        String fullName = patInstance.getPatFirstName().concat(" ").concat(patInstance.getPatLastName());
        String username = patInstance.getPatUsername();
        String email = patInstance.getPatEmail();
        String phoneNum = patInstance.getPatPhoneNum();
        String dob = patInstance.getPatDob();
        String amka = patInstance.getPatSecuredNum();

        System.out.println("Intent Print : " + fullName+ " "+username+
                " " +email+ " " +phoneNum+ " " +dob + " " +amka);

        fullNameTxt.setText(fullName);
//        lastNameTxt.setText(lastName);
        usernameTxt.setText(username);
        emailTxt.setText(email);
        phoneNumTxt.setText(phoneNum);
        dobTxt.setText(dob);
        amkaTxt.setText(amka);


        editPatProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnState){
                    emailTxt.setEnabled(true);
                    phoneNumTxt.setEnabled(true);
//                    amkaTxt.setEnabled(true);
                    dobTxt.setEnabled(true);

                    editPatProfileBtn.setText("Save Changes");
                    btnState = true;
                    System.out.println("[DEBUG NEW] Edit profile text: changed, btnState: true, MaterialEditText: enabled");
                }else {
                    String updatedEmail = emailTxt.getText().toString();
                    String updatedPhoneNum = phoneNumTxt.getText().toString();
//                    String updatedAmka = amkaTxt.getText().toString();
                    String updatedBirthdate = dobTxt.getText().toString();
                    System.out.println("[DEBUG NEW 1] Updated => Email: " + updatedEmail + " PhoneNum: " + updatedPhoneNum +
                            " BirthDate: " + updatedBirthdate);

                    patInstance.setPatEmail(updatedEmail);
                    patInstance.setPatPhoneNum(updatedPhoneNum);
                    patInstance.setPatDob(updatedBirthdate);

                    System.out.println("DEBUG NEW 2] Updated Patient Object: " + patInstance.toString());

                    System.out.println("[DEBUG NEW 3] Updated => PatientId: " + patInstance.getPatientId() +
                            " Full Name: " + patInstance.getPatFirstName() + " " + patInstance.getPatLastName() +
                            " Username: " + patInstance.getPatUsername() +
                            " Email: " + patInstance.getPatEmail() +
                            " Phone Num: " + patInstance.getPatPhoneNum() +
                            " Pat AMKA: " + patInstance.getPatSecuredNum() +
                            " Birthdate: " + patInstance.getPatDob());

                    updatedFields = new HashMap<>();
                    updatedFields.put("email", patInstance.getPatEmail());
                    updatedFields.put("phoneNum", patInstance.getPatPhoneNum());
                    updatedFields.put("dob", patInstance.getPatDob());

                    //DONE: Method for Update API Call
                    updatePatient(patInstance.getPatientId(), updatedFields);

                    emailTxt.setEnabled(false);
                    phoneNumTxt.setEnabled(false);
//                    amkaTxt.setEnabled(true);
                    dobTxt.setEnabled(false);

                    editPatProfileBtn.setText("Edit Profile");
                    btnState = false;
                }

            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DONE: Delete API Request
                deletePatient(patInstance.getPatientId());
            }
        });

    }

    private void deletePatient(Integer patientId) {
        System.out.println("[Pat 1] Inside deletePatient() before retrofit initialization -> patient ID = " + patientId);
        RetrofitService retrofitPatService = new RetrofitService();
        System.out.println("[Pat 1.1] API initialization = ");
        PatientApi patientApi = retrofitPatService.getRetrofit().create(PatientApi.class);
        patientApi.deletePatient(patientId).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()){
                    Toast.makeText(PatDetails.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("Patient Update", "Updated Patient: " + response.toString());
                    Intent goToWelcomeScreen = new Intent(PatDetails.this, MainActivity.class);
                    goToWelcomeScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(goToWelcomeScreen);
                    finish();
                }else {
                    // Handle the error
                    Toast.makeText(PatDetails.this, response.body() +" & " + response.code(), Toast.LENGTH_LONG).show();
                    Log.e("Patient DELETE", "Failed to DELETE patient. Response code: " + response.code() + " & " + response.body());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(PatDetails.this, "Request Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("[ 2] TAG: " + " onFailure: ", "The Message is : " + t.getMessage());
            }
        });
    }

    private void updatePatient(Integer patientId, Map<String, Object> updatedFields) {
        System.out.println("[Pat 2] Inside updatePatient() before retrofit initialization -> patient = " + updatedFields);
        RetrofitService retrofitPatService = new RetrofitService();
        System.out.println("[Pat 2.1] API initialization = ");
        PatientApi patientApi = retrofitPatService.getRetrofit().create(PatientApi.class);
        patientApi.updatePatient(patientId, updatedFields).enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if (response.isSuccessful()){
                    Patient responsePat = response.body();
                    if (responsePat != null){
                        Log.d("Patient Update", "Updated Patient: " + responsePat.toString());
                    }
                }else {
                    // Handle the error
                    Log.e("Patient Update", "Failed to update patient. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Toast.makeText(PatDetails.this, "Request Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("[ 2] TAG: " + " onFailure: ", "The Message is : " + t.getMessage());
            }
        });
    }
}