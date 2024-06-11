package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.DoctorApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //For testing puposes
//        setContentView(R.layout.activity_log_in);
//        setContentView(R.layout.activity_user_type);
//        setContentView(R.layout.activity_doc_sign_in);
//        setContentView(R.layout.activity_doc_details);
//        setContentView(R.layout.activity_pat_sign_in);
//        setContentView(R.layout.activity_pat_details);

        enter = findViewById(R.id.enterBtn);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent enterApplication = new Intent(MainActivity.this, LogInActivity.class);
                //TEST
//                Intent enterApplication = new Intent(MainActivity.this, DocDetails.class);
//                enterApplication.putExtra();
                startActivity(enterApplication);
            }
        });
        //TEST METHOD -> THIS WORKED WITH INTENT AND SUCCESSFULL GET REQUEST
//        detailsClassCall();
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
