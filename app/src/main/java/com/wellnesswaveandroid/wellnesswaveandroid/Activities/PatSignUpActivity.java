package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.PatientApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.FieldsValidators;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatSignUpActivity extends AppCompatActivity {

    private MaterialAutoCompleteTextView patFirstName, patLastName, patUsername, patPassword, patEmail, patAmka, patPhoneNum, patDob;
    private TextView fnameErrMessageTxt, lnameErrMessageTxt, usernameErrMessageTxt, passErrMessageTxt, emailErrMessageTxt, amkaErrMessageTxt, phoneErrMessageTxt, dobErrMessageTxt;
    private Button savePatient;
    private RetrofitService retrofitService;
    private Patient patient;
    private FieldsValidators fieldsValidators;

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

        fnameErrMessageTxt = findViewById(R.id.fNamePatErrorTxtView);
        lnameErrMessageTxt = findViewById(R.id.lNamePatErrorTxtView);
        usernameErrMessageTxt = findViewById(R.id.usernamePatErrorTxtView);
        passErrMessageTxt = findViewById(R.id.passPatErrorTxtView);
        emailErrMessageTxt = findViewById(R.id.emailPatErrorTxtView);
        amkaErrMessageTxt = findViewById(R.id.amkaPatErrorTxtView);
        phoneErrMessageTxt = findViewById(R.id.phonePatErrorTxtView);
        dobErrMessageTxt = findViewById(R.id.dobPatErrorTxtView);


        //initialization: find views by id for save button
        savePatient = findViewById(R.id.patSaveBtn);
        fieldsValidators = new FieldsValidators();

        savePatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fields validation so the input is right
//                patientsFieldsValidation();
                /*** First Name Validation */
                String firstNameErrorMessage = fieldsValidators.validateFirstName(patFirstName.getText().toString().trim());
                fnameErrMessageTxt.setVisibility(View.VISIBLE);
                fnameErrMessageTxt.setText(firstNameErrorMessage);
                fnameErrMessageTxt.setTextColor(fnameErrMessageTxt.getContext().getColor(R.color.errorred));

                /*** Last Name Validation */
                String lastNameErrorMessage = fieldsValidators.validateLastName(patLastName.getText().toString().trim());
                lnameErrMessageTxt.setVisibility(View.VISIBLE);
                lnameErrMessageTxt.setText(lastNameErrorMessage);
                lnameErrMessageTxt.setTextColor(lnameErrMessageTxt.getContext().getColor(R.color.errorred));

                /*** Username Validation */
                String usernameErrorMessage = fieldsValidators.validateUsername(patUsername.getText().toString().trim());
                usernameErrMessageTxt.setVisibility(View.VISIBLE);
                usernameErrMessageTxt.setText(usernameErrorMessage);
                usernameErrMessageTxt.setTextColor(usernameErrMessageTxt.getContext().getColor(R.color.errorred));

                /*** Password Validation */
                String passErrorMessage = fieldsValidators.validatePassword(patPassword.getText().toString().trim());
                passErrMessageTxt.setVisibility(View.VISIBLE);
                passErrMessageTxt.setText(passErrorMessage);
                passErrMessageTxt.setTextColor(passErrMessageTxt.getContext().getColor(R.color.errorred));

                /*** Email Validation */
                String emailErrorMessage = fieldsValidators.validateEmail(patEmail.getText().toString().trim());
                emailErrMessageTxt.setVisibility(View.VISIBLE);
                emailErrMessageTxt.setText(emailErrorMessage);
                emailErrMessageTxt.setTextColor(emailErrMessageTxt.getContext().getColor(R.color.errorred));

                /*** AMKA Validation */
                String amkaErrorMessage = fieldsValidators.validateAmka(patAmka.getText().toString().trim());
                amkaErrMessageTxt.setVisibility(View.VISIBLE);
                amkaErrMessageTxt.setText(amkaErrorMessage);
                amkaErrMessageTxt.setTextColor(amkaErrMessageTxt.getContext().getColor(R.color.errorred));

                /*** Phone Number Validation */
                String phoneNumberErrorMessage = fieldsValidators.validatePhoneNumber(patPhoneNum.getText().toString().trim());
                phoneErrMessageTxt.setVisibility(View.VISIBLE);
                phoneErrMessageTxt.setText(phoneNumberErrorMessage);
                phoneErrMessageTxt.setTextColor(phoneErrMessageTxt.getContext().getColor(R.color.errorred));

                /*** Profession Validation */
                String dobErrorMessage = fieldsValidators.validateDob(patDob.getText().toString().trim());
                dobErrMessageTxt.setVisibility(View.VISIBLE);
                dobErrMessageTxt.setText(dobErrorMessage);
                dobErrMessageTxt.setTextColor(dobErrMessageTxt.getContext().getColor(R.color.errorred));


                /**
                 * Validation to proceed with request if all this errors occur
                 */
                boolean empty = fieldsValidators.validateEmptyPat(patFirstName.getText().toString().trim(), patLastName.getText().toString().trim(),
                        patUsername.getText().toString().trim(), patPassword.getText().toString().trim(),
                        patEmail.getText().toString().trim(), patAmka.getText().toString().trim(), patPhoneNum.getText().toString().trim(),
                        patDob.getText().toString().trim());
                boolean regex = fieldsValidators.validateRegex(patPassword.getText().toString().trim(), patEmail.getText().toString().trim(),
                        patPhoneNum.getText().toString().trim(), patAmka.getText().toString().trim());
                boolean length = fieldsValidators.validateLength(patPassword.getText().toString().trim(), patPhoneNum.getText().toString().trim(),
                        patAmka.getText().toString().trim());

                if (empty){
                    Toast.makeText(PatSignUpActivity.this, "Please fill in the empty fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (regex) {
                    System.out.println("[Validation Debug]: regex= " + regex + " & length= " + length);
                    Toast.makeText(PatSignUpActivity.this, "Please check the format of fields marked with error message.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (length) {
                    Toast.makeText(PatSignUpActivity.this, "Please check the text length of fields marked with error message.", Toast.LENGTH_SHORT).show();
                    return;
                }

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
                patient = new Patient(firstName, lastName, username, password, email, phoneNum, securedNum, dob, type);

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
                            Intent goToSignIn = new Intent(PatSignUpActivity.this, MainActivity.class);
                            startActivity(goToSignIn);
//                            Toast.makeText(PatSignUpActivity.this, response.body().getPatientId(), Toast.LENGTH_LONG).show();
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