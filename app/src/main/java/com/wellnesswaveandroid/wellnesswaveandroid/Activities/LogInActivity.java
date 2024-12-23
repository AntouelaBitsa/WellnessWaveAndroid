package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.gson.Gson;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.LogInDtoApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.LogInDTO;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.SplitJSONImpl;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    private Button signInButton;
    private MaterialAutoCompleteTextView edtUsername, edtPassword;
    private Doctor doc;
    private RetrofitService retrofitService;
    private SplitJSONImpl splitJSON = new SplitJSONImpl();
    //DONE: private Patient pat;
    private Patient pat;
    private LogInDTO logInDTO;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //setting the values to the activity components
//        docUsername = findViewById(R.id.usernameEdtTxt);
//        docPassword = findViewById(R.id.passwordEdtTxt);

        //initialization: find views by id for username & password
        edtUsername = findViewById(R.id.usernameEdtTxt);
        edtPassword = findViewById(R.id.passwordEdtTxt);

        //DONE : implementation Log In Button
        signInButton = findViewById(R.id.signInBtn);
        System.out.println("before onClick : OK0");

        signInButton.setOnClickListener(new View.OnClickListener() {
            //DONE user validation
            //DONE: implementation of get request and pass data to DocDetails activity so we can see the data
            @Override
            public void onClick(View view) {
                System.out.println("Inside onClick : OK1");
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                logInDTOSession(username, password);
            }
        });

//        //DONE : intent for UserTypeActivity
//        signInButton = findViewById(R.id.signInBtn);
//        signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent userType = new Intent(LogInActivity.this, UserTypeActivity.class);
//                startActivity(userType);
//            }
//        });
    }

    private void logInDTOSession(String username, String password) {
        System.out.println("OK1.1");
        LogInDTO logInSession = new LogInDTO(username, password);

        retrofitService = new RetrofitService();
        LogInDtoApi logInDtoApi = retrofitService.getRetrofit().create(LogInDtoApi.class);
        System.out.println("OK1.2");

        logInDtoApi.logIN(logInSession).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                System.out.println("Inside onResponse : OK2");
                if (!response.isSuccessful() || response.body() == null) {
                    System.out.println("Response Error: " + response.code() + " - " + response.message().toString());
                }

                System.out.println(":Before sessionHelper : OK3");
                Result userSessionHelper = response.body();
                int status = userSessionHelper.getStatus(); //TEST
                String userJSON = userSessionHelper.getMessage(); //TEST
                System.out.println(">>> PRINT response.body() : " + response.body().toString() + " - " + status + " - " + userJSON); //TEST

                if (userSessionHelper.getMessage() == null){
                    System.out.println("Inside if : NOT OK");
                    System.out.println("userSession Json " + userSessionHelper.getMessage());
                    Log.d(TAG, "onResponse: FAILED " + response.body().toString() + " " + response.code());
                    return;
                } else if (userSessionHelper.getStatus() == 1){
                    Toast.makeText(LogInActivity.this, "User Doesn't exist", Toast.LENGTH_LONG).show();
                    System.out.println("POST response : " + userSessionHelper.getMessage() + " and status = " + userSessionHelper.getStatus());
                    return;
                }
                System.out.println("Before Intent : OK4");
                System.out.println("-->> Successfull USER LOG IN ");

                //DONE: check for user type == 2 to be a patient
                //DONE : call SpliJSONImpl class to get the user type
                int userType = splitJSON.extractUserType(userJSON);
                if(userType == 1) {
                    docIntentImplementation(userJSON);
                }else if (userType == 2) {
                    patIntentImplementation(userJSON);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                System.out.println("onFailure: NOT OK");
                Toast.makeText(LogInActivity.this, "Request Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("onFailure", "onResponse: " + t.getMessage());

            }
        });
    }


    //DONE : docIntentImplementation()
    private void docIntentImplementation(String userJSON) {
        Doctor docInstance = splitJSON.extractDocFromJson(userJSON);
        System.out.println("[D] TEST PRINT: " + userJSON.toString());
        System.out.println("Doctor intent implementation... GSON");
        Doctor.getInstance().setDoctorData(docInstance); //test
//        Gson gson = new Gson();
//        Doctor tempDoc = gson.fromJson(userJSON, Doctor.class);
//        Doctor docInstance = Doctor.getInstance();
//        docInstance.setDoctorData(tempDoc);
        System.out.println("Singleton Doctor after LOGIN :  " + docInstance.toString());
        System.out.println("[D] docIntentImplementation : " + docInstance.getDocEmail()+ " " + docInstance.toString());
//        Intent logInDocUser = new Intent(LogInActivity.this, DocDetails.class);
        Intent logInDocUser = new Intent(LogInActivity.this, DoctorHomePageActivity.class);
        logInDocUser.putExtra("doc_id", docInstance.getDocId()); //test
        logInDocUser.putExtra("first_name", docInstance.getDocFirstName()); // Pass data to next activity -> must pass each filed seperatly
        logInDocUser.putExtra("last_name", docInstance.getDocLastName());
        logInDocUser.putExtra("username", docInstance.getDocUsername());
        logInDocUser.putExtra("email", docInstance.getDocEmail());
        logInDocUser.putExtra("phone", docInstance.getDocPhoneNum());
        logInDocUser.putExtra("prof", docInstance.getDocProfession());
        logInDocUser.putExtra("address", docInstance.getDocAddress());
        startActivity(logInDocUser);

    }

    //DONE : patIntentImplementation()
    private void patIntentImplementation(String userJSON){
        System.out.println("[P] TEST PRINT: " + userJSON.toString());
        Gson gson = new Gson();
        Patient tempPat = gson.fromJson(userJSON, Patient.class);
        Patient patient = Patient.getInstance();
        patient.setPatientData(tempPat);
        System.out.println("[P] patIntentImplementation : " + patient.getPatEmail()+ " " + patient.toString());
//        Intent logInPatUser = new Intent(LogInActivity.this, PatDetails.class);
        Intent logInPatUser = new Intent(LogInActivity.this, PatientHomePageActivity.class);
        logInPatUser.putExtra("first_namep", patient.getPatFirstName()); // Pass data to next activity -> must pass each filed seperatly
        logInPatUser.putExtra("last_namep", patient.getPatLastName());
        logInPatUser.putExtra("usernamep", patient.getPatUsername());
        logInPatUser.putExtra("emailp", patient.getPatEmail());
        logInPatUser.putExtra("phonep", patient.getPatPhoneNum());
        logInPatUser.putExtra("dobp", patient.getPatDob());
        startActivity(logInPatUser);
    }

}