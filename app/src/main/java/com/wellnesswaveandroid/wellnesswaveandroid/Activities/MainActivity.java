package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wellnesswaveandroid.wellnesswaveandroid.R;

public class MainActivity extends AppCompatActivity {

    private Button signInNavToActivity, signUpNavToActivity;
    private BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInNavToActivity = findViewById(R.id.signInMain);
        signInNavToActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navigateToSignIn = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(navigateToSignIn);
//                TEST BookAppointments
//                Intent navigateToSignIn = new Intent(MainActivity.this, BookAppointmentActivity.class);
//                startActivity(navigateToSignIn);
//                TEST DocDetails
//                Intent navigateToSignIn = new Intent(MainActivity.this, DocDetails.class);
//                navigateToSignIn.putExtra();
            }
        });

        signUpNavToActivity = findViewById(R.id.signUpHereTxtBtn);
        signUpNavToActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigateToUserType = new Intent(MainActivity.this, UserTypeActivity.class);
                startActivity(navigateToUserType);
            }
        });
    }

    //THE RIGHT WAY FOR DETAILS ACTIVITIES
//    private void detailsClassCall() {
//        enter = findViewById(R.id.enterBtn);
//
//        enter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //declaring local variables
//                TextView firstNameTxt, lastNameTxt, usernameTxt, passwordTxt, emailTxt, amkaTxt, phoneNumTxt, professionTxt, addressTxt;
//                RetrofitService retrofitService;
//
//                Intent intent = new Intent(MainActivity.this, DocDetails.class);
//
//                //initialization: find views by id for edit texts
//                firstNameTxt = findViewById(R.id.docFNameTxt);
//                lastNameTxt = findViewById(R.id.docLNameTxt);
//                usernameTxt = findViewById(R.id.docUsernameTxt);
//                passwordTxt = findViewById(R.id.docPasswordTxt);
//                emailTxt = findViewById(R.id.docEmailTxt);
//                amkaTxt = findViewById(R.id.docAmkaTxt);
//                phoneNumTxt = findViewById(R.id.docPhoneNumTxt);
//                professionTxt = findViewById(R.id.docProfessionTxt);
//                addressTxt = findViewById(R.id.docAddressTxt);
//
//                Log.d("Are HEre", " Yes");
//                retrofitService = new RetrofitService();
//                DoctorApi doctorApi = retrofitService.getRetrofit().create(DoctorApi.class);
//                Integer docId = 1;
//                doctorApi.getDoctorById(docId).enqueue(new Callback<Doctor>() {
//                    @Override
//                    public void onResponse(Call<Doctor> call, Response<Doctor> response) {
//                        if (!response.isSuccessful()) {
////                            Toast.makeText(MainActivity.this, "Failed Get Request", Toast.LENGTH_LONG).show();
////                            Log.d("On Response Details", "onResponse: " + response.code() + "  " + response.body());
//                        }
//                        Log.d("Getting the response", "SUCCESS");
//                        //getting the response to String values
//
//                        Doctor responseDoc = response.body();
//                        System.out.println("Doctor DETAILS:  " + responseDoc.toString());
//                        Log.d("DOC DETAILS : ", responseDoc.toString());
//
//                        String firstName = responseDoc.getDocFirstName().toString();
//                        String lastName = responseDoc.getDocLastName().toString();
//                        String username = responseDoc.getDocUsername().toString();
//                        String password = responseDoc.getDocPassword().toString();
//                        String email = responseDoc.getDocEmail().toString();
//                        String amka = responseDoc.getDocSecuredNum().toString();
//                        String phoneNum = responseDoc.getDocPhoneNum().toString();
//                        String profession = responseDoc.getDocProfession().toString();
//                        String address = responseDoc.getDocAddress().toString();
//
//                        intent.putExtra("first_name", firstName);
//                        intent.putExtra("last_name", lastName);
//                        intent.putExtra("usernm", username);
//                        intent.putExtra("pass", password);
//                        intent.putExtra("email", email);
//                        intent.putExtra("amka", amka);
//                        intent.putExtra("phone", phoneNum);
//                        intent.putExtra("prof", profession);
//                        intent.putExtra("address", address);
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void onFailure(Call<Doctor> call, Throwable t) {
//                        Toast.makeText(MainActivity.this, "Request Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
//                        Log.d(TAG + "onFailure", "onResponse: " + t.getMessage());
//                    }
//                });
//            }
//        });
//    }
}
