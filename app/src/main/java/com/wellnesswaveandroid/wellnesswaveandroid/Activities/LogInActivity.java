package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.DoctorApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.SplitJSONImpl;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    private Button logInButton, signInButton;
    private MaterialAutoCompleteTextView edtUsername, edtPassword;
    private Doctor doc;
    private RetrofitService retrofitService;
    private SplitJSONImpl splitJSON = new SplitJSONImpl();
    //TODO: private Patient pat;

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
        logInButton = findViewById(R.id.logInBtn);
        System.out.println("before onClick : OK0");

        logInButton.setOnClickListener(new View.OnClickListener() {
            //TODO user validation
            //TODO: implementation of get request and pass data to DocDetails activity so we can se the data
            @Override
            public void onClick(View view) {
                System.out.println("Inside onClick : OK1");
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                System.out.println("OK1.1");
                retrofitService = new RetrofitService();
                DoctorApi doctorApi = retrofitService.getRetrofit().create(DoctorApi.class);

                System.out.println("OK1.2");
                doctorApi.logIN(username, password).enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response)
                    {
                        //TODO : POSTREQUEST result object (code, message)
                        //if statement for successful response and not null body()
                        System.out.println("Inside onResponse : OK2");
                        if (!response.isSuccessful() && response.body() == null){
                            System.out.println(":Before sessionHelper : OK3");
                            Result userSessionHelper = response.body();
                            int status = userSessionHelper.getStatus(); //TEST
                            String userJSON = userSessionHelper.getMessage(); //TEST
                            System.out.println(">>> PRINT response.body() : " + response.body().toString() + " - " + status + " - " + userJSON); //TEST

                            if (userSessionHelper.getMessage() == null){
                                System.out.println("Inside if : NOT OK");
                                System.out.println("userSession Json " + userSessionHelper.getMessage());
                                Log.d(TAG, "onResponse: FAILED " + response.body().toString() + " " + response.code());

                            } else if (userSessionHelper.getStatus() == 1){
                                Toast.makeText(LogInActivity.this, "User Doesn't exist", Toast.LENGTH_LONG).show();
                                System.out.println("POST response : " + userSessionHelper.getMessage() + " and status = " + userSessionHelper.getStatus());

                            }else {
                                System.out.println("Before Intent : OK4");
                                System.out.println("-->> Successfull USER LOG IN ");
                                //TODO: check for user type == 2 to be a patient
                                //TODO : call SpliJSONImpl class to get the user type
                                int userType = splitJSON.extractUserType(userJSON);
                                if(userType == 1) {
                                    docIntentImplementation(userJSON);

                                } //else if (userType == 2) {
//                                    Patient patient = splitJSON.extractPatFromJson(userJSON);
//                                }

//                                Intent logInUser = new Intent(LogInActivity.this, DocDetails.class);
//                                logInUser.putExtra("userSessionJson", userJSON); // Pass data to next activity -> must pass each filed seperatly
//                                startActivity(logInUser);
                            }


                        }else {
                            System.out.println("Response Error: " + response.code() + " - " + response.message().toString());
                        }
                    }

//                    private void docIntentImplementation() {
//                        Doctor doctor = splitJSON.extractDocFromJson(userJSON);
//                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        System.out.println("onFailure: NOT OK");
                        Toast.makeText(LogInActivity.this, "Request Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("onFailure", "onResponse: " + t.getMessage());
                    }
                });
            }
        });

        //DONE : intent for UserTypeActivity
        signInButton = findViewById(R.id.signInBtn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userType = new Intent(LogInActivity.this, UserTypeActivity.class);
                startActivity(userType);
            }
        });
    }

    //TODO : docIntentImplementation()
    private void docIntentImplementation(String userJSON) {
        Doctor doctor = splitJSON.extractDocFromJson(userJSON);
        Intent logInUser = new Intent(LogInActivity.this, DocDetails.class);
        logInUser.putExtra("first_name", doctor.getDocFirstName()); // Pass data to next activity -> must pass each filed seperatly
        logInUser.putExtra("last_name", doctor.getDocLastName());
        logInUser.putExtra("username", doctor.getDocUsername());
        logInUser.putExtra("email", doctor.getDocEmail());
        logInUser.putExtra("phone", doctor.getDocPhoneNum());
        logInUser.putExtra("prof", doctor.getDocProfession());
        logInUser.putExtra("address", doctor.getDocAddress());
        startActivity(logInUser);

    }

    private String jsonSplitToStringFields(String userJSON) {
        String usrType = null;
        //TODO : add println for testing and debugging
        //TODO : split JSON string object into string variables based on fields
        //TODO : create string values
        //TODO : create Doctor Object - in another method
        //TODO : intent implementation - putExtra() - in another method
        return usrType;
    }



    //TODO : patIntentImplementation()
}