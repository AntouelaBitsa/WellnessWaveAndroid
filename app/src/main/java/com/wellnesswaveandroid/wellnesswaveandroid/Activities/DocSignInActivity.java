package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.DoctorApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocSignInActivity extends AppCompatActivity {

    MaterialAutoCompleteTextView docFirstName, docLastName, docUsername, docPassword, docEmail, docAmka, docPhoneNum, docProfession, docAddress;
    Button saveDoctor;
    RetrofitService retrofitService;
    Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_sign_in);

        //For Debuging Purposes
        Log.d("DOC-SIGN-IN", "Component Initialization");

        //initialization: find views by id for edit texts
        docFirstName = findViewById(R.id.docFNameEdt);
        docLastName = findViewById(R.id.docLNameEdt);
        docUsername = findViewById(R.id.docUsernameEdt);
        docPassword = findViewById(R.id.docPasswordEdt);
        docEmail = findViewById(R.id.docEmailEdt);
        docAmka = findViewById(R.id.docAmkaEdt);
        docPhoneNum = findViewById(R.id.docPhoneNumEdt);
        docProfession = findViewById(R.id.docProfessionEdt);
        docAddress = findViewById(R.id.docAddressEdt);

        //initialization: find views by id for save button
        saveDoctor = findViewById(R.id.docSaveBtn);

        saveDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDoctorData();
            }

            private void postDoctorData() {
                //Setting Variables String from edit text
                String firstName = String.valueOf(docFirstName.getText());
                String lastName = String.valueOf(docLastName.getText());
                String username = String.valueOf(docUsername.getText());
                String password = String.valueOf(docPassword.getText());
                String email = String.valueOf(docEmail.getText());
                String securedNum = String.valueOf(docAmka.getText());
                String phoneNum = String.valueOf(docPhoneNum.getText());
                String profession = String.valueOf(docProfession.getText());
                String address = String.valueOf(docAddress.getText());
                int type = 1; //code for doc

                //setting values to object doctor
                doctor = new Doctor(firstName, lastName, username, password, email, securedNum, phoneNum, profession,address,type);

                //setting retrofit service
                retrofitService = new RetrofitService();

                //setting doctor's api
                DoctorApi doctorApi = retrofitService.getRetrofit().create(DoctorApi.class);

                //calling enqueue
                doctorApi.createDoctor(doctor).enqueue(new Callback<Doctor>() {
                    @Override
                    public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG + "SUCESS", "onResponse: " + response.code());
                            Toast.makeText(DocSignInActivity.this, response.body().getDocId(), Toast.LENGTH_LONG).show();;
                        }else {
                            Log.d(TAG, "onResponse: FAILED" + response.body() + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Doctor> call, Throwable t) {
                        Toast.makeText(DocSignInActivity.this, "Request Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG + "onFailure", "onResponse: " + t.getMessage());
                    }
                });
            }
        });
    }
}
