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
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.DoctorApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocSignUpActivity extends AppCompatActivity {

    private MaterialAutoCompleteTextView docFirstName, docLastName, docUsername, docPassword, docEmail, docAmka, docPhoneNum, docProfession, docAddress;
    private Button saveDoctor;
    private RetrofitService retrofitService;
    private Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_sign_up);

        //For Debuging Purposes
        Log.d("DOC-SIGN-IN", "Component Initialization");

        //initialization: find views by id for edit texts from material design library
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
                //fields validation so the input is right
                fieldsValidation();

                //call of post request for user registration
                postDoctorData();
//                Intent goToSignIn = new Intent(DocSignUpActivity.this, MainActivity.class);
//                startActivity(goToSignIn);
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
                            Intent goToSignIn = new Intent(DocSignUpActivity.this, MainActivity.class);
                            startActivity(goToSignIn);
                            Log.d(TAG + "SUCESS", "onResponse: " + response.code());
//                            Toast.makeText(DocSignUpActivity.this, response.body().getDocId(), Toast.LENGTH_LONG).show();;
                            finish();
                        }else {
                            Log.d(TAG, "onResponse: FAILED" + response.body() + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Doctor> call, Throwable t) {
                        Toast.makeText(DocSignUpActivity.this, "Request Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG + "onFailure", "The Message is : " + t.getMessage());
                    }
                });
            }
        });
    }

    //FIELDS VALIDATION FOR THE RIGHT INPUT
    private void fieldsValidation() {
        //PASSWORD VALIDATION
        docPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (validatePassword() && editable.length()>0){
                    Toast.makeText(DocSignUpActivity.this, "Valid Password", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(DocSignUpActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //EMAIL VALIDATION
        docEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (validateEmail() && editable.length()>0){
                    Toast.makeText(DocSignUpActivity.this, "Valid Email", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(DocSignUpActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //PHONE NUMBER VALIDATION
        docPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (validatePhoneNumber() && editable.length()>0){
                    Toast.makeText(DocSignUpActivity.this, "Valid Phone Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(DocSignUpActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //ADDRESS VALIDATION
        docAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (validateAddress() && editable.length()>0){
                    Toast.makeText(DocSignUpActivity.this, "Valid Address", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(DocSignUpActivity.this, "Invalid Address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //VALIDATION OF PASSWORD, EMAIL, PHONE NUMBER & ADDRESS
    //DONE: Password validation
    private boolean validatePassword(){
        //Password validation characters and rules
        String pass = "^(?=.*[0-9])"            //a digit must occur at least once
                + "(?=.*[a-z])(?=.*[A-Z])"      //a lower case alphabet must occur at least once & an upper case alphabet that must occur at least once
                + "(?=.*[@!^&_])"               //a special character that must occur at least once
                + "(?=\\S+$).{8,15}$";          //white spaces aren’t allowed & at least 8 characters and at most 15 characters
        Pattern passPattern = Pattern.compile(pass);

        String passValidated = docPassword.getText().toString().trim();

        //Checks if the password field is correctly completed
        if (passValidated.isEmpty()){                               //password field can not be empty
            docPassword.setError("Field can not be empty");
            return false;
        } else if (!passPattern.matcher(passValidated).matches()) {     //password field must follow all the rules
            docPassword.setError("Password is too weak");
            return false;
        }else{                                                      //password field is correctly completed
            docPassword.setError(null);
            return true;
        }
    }

    //DONE: Email validation
    private boolean validateEmail(){
        String email = "^[a-zA-Z0-9.%+-]"
                + "+@[a-zA-Z0-9.-]"
                + "+\\.[a-zA-Z]{2,}$";
        Pattern emailPattern = Pattern.compile(email);

        String emailValidated = docEmail.getText().toString().trim();

        //Checks if the password field is correctly completed
        if (emailValidated.isEmpty()){                                     //email field can not be empty
            docEmail.setError("Field can not be empty");
            return false;
        } else if (!emailPattern.matcher(emailValidated).matches()) {     //email field must follow all the rules
            docEmail.setError("Wrong email syntax! It must be example@domain.com");
            return false;
        }else{                                                            //email field is correctly completed
            docEmail.setError(null);
            return true;
        }
    }

    //DONE: Phone number validation
    private boolean validatePhoneNumber(){
        String phoneNumber = "^\\+[1-9\\d{1,10}$]";
        Pattern phonePattern = Pattern.compile(phoneNumber);

        String phoneNumberValidated = docPhoneNum.getText().toString().trim();

        //Checks if the password field is correctly completed
        if (phoneNumberValidated.isEmpty()){                                     //phone number field can not be empty
            docPhoneNum.setError("Field can not be empty");
            return false;
        } else if (!phonePattern.matcher(phoneNumberValidated).matches()) {     //phone number field must follow all the rules
            docPhoneNum.setError("Wrong phone number! It must contain only numbers," + "\n and the length it has to be 10");
            return false;
        }else{                                                                  //phone number field is correctly completed
            docPhoneNum.setError(null);
            return true;
        }
    }

    //DONE: Address validation
    private boolean validateAddress(){
        String myAddress = "^([A-Za-z\\s\\-']+\\s\\d+),"
                 + "\\s([A-Za-z\\s]+)\\s(\\d{5}),\\s([A-Za-z\\s]+)$";
        Pattern addressPattern = Pattern.compile(myAddress);

        String addressValidated = docAddress.getText().toString().trim();

        //Checks if the password field is correctly completed
        if (addressValidated.isEmpty()){                                      //address field can not be empty
            docAddress.setError("Field can not be empty");
            return false;
        } else if (!addressPattern.matcher(addressValidated).matches()) {     //address field must follow all the rules
            docAddress.setError("Wrong address syntax! It must be: Road No, Area Postal Code, City");
            return false;
        }else{                                                                 //address field is correctly completed
            docAddress.setError(null);
            return true;
        }
    }

}
