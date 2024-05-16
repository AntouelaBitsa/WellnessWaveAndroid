package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.DoctorApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocDetails extends AppCompatActivity {

    TextView firstNameTxt, lastNameTxt, usernameTxt, passwordTxt, emailTxt, amkaTxt, phoneNumTxt, professionTxt, addressTxt;
    RetrofitService retrofitService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_details);

        //initialization: find views by id for edit texts
        firstNameTxt = (TextView) findViewById(R.id.docFNameTxt);
        lastNameTxt = (TextView) findViewById(R.id.docLNameTxt);
        usernameTxt = (TextView) findViewById(R.id.docUsernameTxt);
        passwordTxt = (TextView) findViewById(R.id.docPasswordTxt);
        emailTxt = (TextView) findViewById(R.id.docEmailTxt);
        amkaTxt = (TextView) findViewById(R.id.docAmkaTxt);
        phoneNumTxt = (TextView) findViewById(R.id.docPhoneNumTxt);
        professionTxt = (TextView) findViewById(R.id.docProfessionTxt);
        addressTxt = (TextView) findViewById(R.id.docAddressTxt);

        Log.d("Are HEre", " Yes");
//        TEST Harcoded Data doesn't show in ui
        firstNameTxt.setText("Antoyela");
        lastNameTxt.setText("Antoyela");
        usernameTxt.setText("Antoyela");
        passwordTxt.setText("Antoyela");
        emailTxt.setText("Antoyela");
        amkaTxt.setText("Antoyela");
        phoneNumTxt.setText("Antoyela");
        professionTxt.setText("Antoyela");
        addressTxt.setText("Antoyela");

//        callGetRequestForDetails();
        //TEST INTENT FROM MAIN
        intentGetMethod("Antouela");
    }

    private void intentGetMethod(String testsrt) {
        Intent takeData = getIntent();
        String firstName = takeData.getStringExtra("first_name");
        String lastName = takeData.getStringExtra("last_name");
        String username = takeData.getStringExtra("usernm");
        String password = takeData.getStringExtra("pass");
        String email = takeData.getStringExtra("email");
        String amka = takeData.getStringExtra("amka");
        String phoneNum = takeData.getStringExtra("phone");
        String profession = takeData.getStringExtra("prof");
        String address = takeData.getStringExtra("address");

        System.out.println("Intent Print : " + firstName+lastName+username+password+email+
                amka+phoneNum+profession+address);

//        firstNameTxt.setText(String.valueOf(firstName));
//        lastNameTxt.setText(String.valueOf(lastName));
//        usernameTxt.setText(String.valueOf(username));
//        passwordTxt.setText(String.valueOf(password));
//        emailTxt.setText(String.valueOf(email));
//        amkaTxt.setText(String.valueOf(amka));
//        phoneNumTxt.setText(String.valueOf(phoneNum));
//        professionTxt.setText(String.valueOf(profession));
//        addressTxt.setText(String.valueOf(address));

        firstNameTxt.setText(testsrt);
        lastNameTxt.setText(testsrt);
        usernameTxt.setText(testsrt);
        passwordTxt.setText(testsrt);
        emailTxt.setText(testsrt);
        amkaTxt.setText(testsrt);
        phoneNumTxt.setText(testsrt);
        professionTxt.setText(testsrt);
        addressTxt.setText(testsrt);

    }

    private void callGetRequestForDetails() {
        retrofitService = new RetrofitService();

        DoctorApi doctorApi = retrofitService.getRetrofit().create(DoctorApi.class);

        Integer docId = 1;
        doctorApi.getDoctorById(docId).enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(DocDetails.this, "Failed Get Request", Toast.LENGTH_LONG).show();
                    Log.d("On Response Details", "onResponse: " + response.code() +"  "+ response.body());
                }

                Log.d("Getting the response", "SUCCESS");
                //getting the response to String values

                Doctor responseDoc = response.body();

                System.out.println("Doctor DETAILS:  " +  responseDoc.toString());
                Log.d("DOC DETAILS : " , responseDoc.toString());

                String firstName = responseDoc.getDocFirstName().toString();
                String lastName = responseDoc.getDocLastName().toString();
                String username = responseDoc.getDocUsername().toString();
                String password = responseDoc.getDocPassword().toString();
                String email = responseDoc.getDocEmail().toString();
                String amka = responseDoc.getDocSecuredNum().toString();
                String phoneNum = responseDoc.getDocPhoneNum().toString();
                String profession = responseDoc.getDocProfession().toString();
                String address = responseDoc.getDocAddress().toString();

                Log.d("Setting the response", "SUCCESS");
                //setting the responses to activity textviews
                firstNameTxt.setText(firstName.toString());
                lastNameTxt.setText(String.valueOf(lastName));
                usernameTxt.setText(String.valueOf(username));
                passwordTxt.setText(String.valueOf(password));
                emailTxt.setText(String.valueOf(email));
                amkaTxt.setText(String.valueOf(amka));
                phoneNumTxt.setText(String.valueOf(phoneNum));
                professionTxt.setText(String.valueOf(profession));
                addressTxt.setText(String.valueOf(address));

            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {
                Toast.makeText(DocDetails.this, "Request Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG" + "onFailure", "onResponse: " + t.getMessage());
            }
        });

    }
}