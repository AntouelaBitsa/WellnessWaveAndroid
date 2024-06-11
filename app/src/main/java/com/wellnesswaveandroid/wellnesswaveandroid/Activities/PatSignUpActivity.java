package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.PatientApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatSignUpActivity extends AppCompatActivity {

    private MaterialAutoCompleteTextView patFirstName, patLastName, patUsername, patPassword, patEmail, patAmka, patPhoneNum, patDob;
    private Button savePatient;
    private RetrofitService retrofitService;
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pat_sign_up);

        //For Debuging purposes
        Log.d("PAT-SIGN-UP", "Component Initialization");

        //Initialization : finding views by id for edit texts from material design library
        patFirstName = findViewById(R.id.patFNameEdt);
        patLastName = findViewById(R.id.patLNameEdt);
        patUsername = findViewById(R.id.patUsernameEdt);
        patPassword = findViewById(R.id.patPasswordEdt);
        patEmail = findViewById(R.id.patEmailEdt);
        patAmka = findViewById(R.id.patAmkaEdt);
        patPhoneNum = findViewById(R.id.patPhoneNumEdt);
        patDob = findViewById(R.id.patDobEdt);

        //initialization: find views by id for save button
        savePatient = findViewById(R.id.patSaveBtn);

        savePatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fields validation so the input is right
                patientsFieldsValidation();

                //call of post request for user registration
                postPatientData();
            }

            private void postPatientData() {
                //Setting String Variables from the edit texts views
                String firstName = String.valueOf(patFirstName.getText());
                String lastName = String.valueOf(patLastName.getText());
                String username = String.valueOf(patUsername.getText());
                String password = String.valueOf(patPassword.getText());
                String email = String.valueOf(patEmail.getText());
                String securedNum = String.valueOf(patAmka.getText());
                String phoneNum = String.valueOf(patPhoneNum.getText());
                String dob = String.valueOf(patDob.getText());
                int type = 2; //code for patient

                //setting values to object Patient
                patient = new Patient(firstName, lastName, username, password, email, securedNum, phoneNum, dob, type);

                //setting retrofit service
                retrofitService = new RetrofitService();

                //setting patient's api
                PatientApi patientApi = retrofitService.getRetrofit().create(PatientApi.class);

                //calling enqueue
                patientApi.createPatient(patient).enqueue(new Callback<Patient>() {
                    @Override
                    public void onResponse(Call<Patient> call, Response<Patient> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG + " SUCCESS: ", "onResponse: " + response.body());
                            Toast.makeText(PatSignUpActivity.this, response.body().getPatientId(), Toast.LENGTH_LONG).show();
                        }else {
                            Log.d(TAG + " FAIL: ", "onResponse: FAILED" + response.body() + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Patient> call, Throwable t) {
                        Toast.makeText(PatSignUpActivity.this, "Request Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(TAG + " onFailure: ", "The Message is : " + t.getMessage());
                    }
                });
            }
        });

    }
    //DONE : FIELDS VALIDATION
    private void patientsFieldsValidation() {
        //PASSWORD VALIDATION
        patPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (validatePatPassword() && editable.length()>0){
                    Toast.makeText(PatSignUpActivity.this, "Valid Password", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PatSignUpActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //EMAIL VALIDATION
        patEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (validatePatEmail() && editable.length()>0){
                    Toast.makeText(PatSignUpActivity.this, "Valid Email", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PatSignUpActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //PHONE NUMBER VALIDATION
        patPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (validatePatPhoneNumber() && editable.length()>0){
                    Toast.makeText(PatSignUpActivity.this, "Valid Phone Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PatSignUpActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //VALIDATION OF PASSWORD, EMAIL, PHONE NUMBER & ADDRESS
    //DONE: Password validation
    private boolean validatePatPassword() {
        //Password validation characters and rules
        String pass = "^(?=.*[0-9])"            //a digit must occur at least once
                + "(?=.*[a-z])(?=.*[A-Z])"      //a lower case alphabet must occur at least once & an upper case alphabet that must occur at least once
                + "(?=.*[@!^&_])"               //a special character that must occur at least once
                + "(?=\\S+$).{8,15}$";          //white spaces aren’t allowed & at least 8 characters and at most 15 characters
        Pattern passPattern = Pattern.compile(pass);

        String passValidated = patPassword.getText().toString().trim();

        //Checks if the password field is correctly completed
        if (passValidated.isEmpty()){                               //password field can not be empty
            patPassword.setError("Field can not be empty");
            return false;
        } else if (!passPattern.matcher(passValidated).matches()) {     //password field must follow all the rules
            patPassword.setError("Password is too weak");
            return false;
        }else{                                                      //password field is correctly completed
            patPassword.setError(null);
            return true;
        }
    }

    //DONE: Email validation
    private boolean validatePatEmail() {
        String email = "^[a-zA-Z0-9.%+-]"
                + "+@[a-zA-Z0-9.-]"
                + "+\\.[a-zA-Z]{2,}$";
        Pattern emailPattern = Pattern.compile(email);

        String emailValidated = patEmail.getText().toString().trim();

        //Checks if the password field is correctly completed
        if (emailValidated.isEmpty()){                                     //email field can not be empty
            patEmail.setError("Field can not be empty");
            return false;
        } else if (!emailPattern.matcher(emailValidated).matches()) {     //email field must follow all the rules
            patEmail.setError("Wrong email syntax! It must be example@domain.com");
            return false;
        }else{                                                            //email field is correctly completed
            patEmail.setError(null);
            return true;
        }
    }

    //DONE: Phone number validation
    private boolean validatePatPhoneNumber() {
        String phoneNumber = "^\\+[1-9\\d{1,10}$]";
        Pattern phonePattern = Pattern.compile(phoneNumber);

        String phoneNumberValidated = patPhoneNum.getText().toString().trim();

        //Checks if the password field is correctly completed
        if (phoneNumberValidated.isEmpty()){                                     //phone number field can not be empty
            patPhoneNum.setError("Field can not be empty");
            return false;
        } else if (!phonePattern.matcher(phoneNumberValidated).matches()) {     //phone number field must follow all the rules
            patPhoneNum.setError("Wrong phone number! It must contain only numbers," + "\n and the length it has to be 10");
            return false;
        }else{                                                                  //phone number field is correctly completed
            patPhoneNum.setError(null);
            return true;
        }
    }
}