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

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.DoctorApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.FieldsValidators;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocSignUpActivity extends AppCompatActivity {

    private MaterialAutoCompleteTextView docFirstName, docLastName, docUsername, docPassword, docEmail, docAmka, docPhoneNum, docProfession, docAddress;
    private TextView fnameErrMessageTxt, lnameErrMessageTxt, usernameErrMessageTxt, passErrMessageTxt, emailErrMessageTxt, amkaErrMessageTxt, phoneErrMessageTxt, professionErrMessageTxt, addressErrMessageTxt;
    private Button saveDoctor;
    private RetrofitService retrofitService;
    private Doctor doctor;
    private FieldsValidators fieldsValidators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_sign_up);

        //For Debugging Purposes
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

        fnameErrMessageTxt = findViewById(R.id.fNameDocErrorTxtView);
        lnameErrMessageTxt = findViewById(R.id.lNameDocErrorTxtView);
        usernameErrMessageTxt = findViewById(R.id.usernameDocErrorTxtView);
        passErrMessageTxt = findViewById(R.id.passDocErrorTxtView);
        emailErrMessageTxt = findViewById(R.id.emailDocErrorTxtView);
        amkaErrMessageTxt = findViewById(R.id.amkaDocErrorTxtView);
        phoneErrMessageTxt = findViewById(R.id.phoneDocErrorTxtView);
        professionErrMessageTxt = findViewById(R.id.professionDocErrorTxtView);
        addressErrMessageTxt = findViewById(R.id.addressDocErrorTxtView);

        //initialization: find views by id for save button
        saveDoctor = findViewById(R.id.docSaveBtn);
        fieldsValidators = new FieldsValidators();

        saveDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fields validation so the input is right
                //TODO: This method needs all EditTexts to be send in the Fields Validators Class
                //TODO: set the error message and the color to the errorTextView
                /*** First Name Validation */
                String firstNameErrorMessage = fieldsValidators.validateFirstName(docFirstName.getText().toString().trim());
                fnameErrMessageTxt.setVisibility(View.VISIBLE);
                fnameErrMessageTxt.setText(firstNameErrorMessage);
                fnameErrMessageTxt.setTextColor(fnameErrMessageTxt.getContext().getColor(R.color.errorred));

                /*** Last Name Validation */
                String lastNameErrorMessage = fieldsValidators.validateLastName(docLastName.getText().toString().trim());
                lnameErrMessageTxt.setVisibility(View.VISIBLE);
                lnameErrMessageTxt.setText(lastNameErrorMessage);
                lnameErrMessageTxt.setTextColor(lnameErrMessageTxt.getContext().getColor(R.color.errorred));

                /*** Username Validation */
                String usernameErrorMessage = fieldsValidators.validateUsername(docUsername.getText().toString().trim());
                usernameErrMessageTxt.setVisibility(View.VISIBLE);
                usernameErrMessageTxt.setText(usernameErrorMessage);
                usernameErrMessageTxt.setTextColor(usernameErrMessageTxt.getContext().getColor(R.color.errorred));

                /*** Password Validation */
                String passErrorMessage = fieldsValidators.validatePassword(docPassword.getText().toString().trim());
                passErrMessageTxt.setVisibility(View.VISIBLE);
                passErrMessageTxt.setText(passErrorMessage);
                passErrMessageTxt.setTextColor(passErrMessageTxt.getContext().getColor(R.color.errorred));

                /*** Email Validation */
                String emailErrorMessage = fieldsValidators.validateEmail(docEmail.getText().toString().trim());
                emailErrMessageTxt.setVisibility(View.VISIBLE);
                emailErrMessageTxt.setText(emailErrorMessage);
                emailErrMessageTxt.setTextColor(emailErrMessageTxt.getContext().getColor(R.color.errorred));

                /*** AMKA Validation */
                String amkaErrorMessage = fieldsValidators.validateAmka(docAmka.getText().toString().trim());
                amkaErrMessageTxt.setVisibility(View.VISIBLE);
                amkaErrMessageTxt.setText(amkaErrorMessage);
                amkaErrMessageTxt.setTextColor(amkaErrMessageTxt.getContext().getColor(R.color.errorred));

                /*** Phone Number Validation */
                String phoneNumberErrorMessage = fieldsValidators.validatePhoneNumber(docPhoneNum.getText().toString().trim());
                phoneErrMessageTxt.setVisibility(View.VISIBLE);
                phoneErrMessageTxt.setText(phoneNumberErrorMessage);
                phoneErrMessageTxt.setTextColor(phoneErrMessageTxt.getContext().getColor(R.color.errorred));

                /*** Profession Validation */
                String professionErrorMessage = fieldsValidators.validateProfession(docProfession.getText().toString().trim());
                professionErrMessageTxt.setVisibility(View.VISIBLE);
                professionErrMessageTxt.setText(professionErrorMessage);
                professionErrMessageTxt.setTextColor(professionErrMessageTxt.getContext().getColor(R.color.errorred));

                /*** Address Validation */
                String addressErrorMessage = fieldsValidators.validateAddress(docAddress.getText().toString().trim());
                addressErrMessageTxt.setVisibility(View.VISIBLE);
                addressErrMessageTxt.setText(addressErrorMessage);
                addressErrMessageTxt.setTextColor(addressErrMessageTxt.getContext().getColor(R.color.errorred));

                /**
                 * Validation to proceed with request if all this errors occur
                 */
                boolean empty = fieldsValidators.validateEmptyDoc(docFirstName.getText().toString().trim(), docLastName.getText().toString().trim(),
                        docUsername.getText().toString().trim(), docPassword.getText().toString().trim(),
                        docEmail.getText().toString().trim(), docAmka.getText().toString().trim(), docPhoneNum.getText().toString().trim(),
                        docProfession.getText().toString().trim(), docAddress.getText().toString().trim());
                boolean regex = fieldsValidators.validateRegex(docPassword.getText().toString().trim(), docEmail.getText().toString().trim(),
                        docPhoneNum.getText().toString().trim(), docAmka.getText().toString().trim());
                boolean length = fieldsValidators.validateLength(docPassword.getText().toString().trim(), docPhoneNum.getText().toString().trim(),
                        docAmka.getText().toString().trim());

                if (empty){
                    Toast.makeText(DocSignUpActivity.this, "Please fill in the empty fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (regex) {
                    System.out.println("[Validation Debug]: regex= " + regex + " & length= " + length);
                    Toast.makeText(DocSignUpActivity.this, "Please check the format of fields marked with error message.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (length) {
                    Toast.makeText(DocSignUpActivity.this, "Please check the text length of fields marked with error message.", Toast.LENGTH_SHORT).show();
                    return;
                }

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
                doctor = new Doctor(firstName, lastName, username, password, email, securedNum, phoneNum, profession, address, type);

                //setting retrofit service
                retrofitService = new RetrofitService();

                //setting doctor's api
                DoctorApi doctorApi = retrofitService.getRetrofit().create(DoctorApi.class);

                //calling enqueue
                doctorApi.createDoctor(doctor).enqueue(new Callback<Doctor>() {
                    @Override
                    public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                        if (response.isSuccessful()) {
                            Intent goToSignIn = new Intent(DocSignUpActivity.this, MainActivity.class);
                            startActivity(goToSignIn); 
                            Log.d(TAG + "SUCESS", "onResponse: " + response.code());
//                            Toast.makeText(DocSignUpActivity.this, response.body().getDocId(), Toast.LENGTH_LONG).show();;
                            finish();
                        } else {
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
//    public void fieldsValidation() {
//        //PASSWORD VALIDATION
//        docPassword.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (validatePassword() && editable.length()>0){
//                    Toast.makeText(DocSignUpActivity.this, "Valid Password", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(DocSignUpActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        //EMAIL VALIDATION
//        docEmail.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (validateEmail() && editable.length()>0){
//                    Toast.makeText(DocSignUpActivity.this, "Valid Email", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(DocSignUpActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        //PHONE NUMBER VALIDATION
//        docPhoneNum.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (validatePhoneNumber() && editable.length()>0){
//                    Toast.makeText(DocSignUpActivity.this, "Valid Phone Number", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(DocSignUpActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        //ADDRESS VALIDATION
//        docAddress.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (validateAddress() && editable.length()>0){
//                    Toast.makeText(DocSignUpActivity.this, "Valid Address", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(DocSignUpActivity.this, "Invalid Address", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

//    //VALIDATION OF PASSWORD, EMAIL, PHONE NUMBER & ADDRESS
//    //DONE: Password validation
//    private boolean validatePassword(){
//
//
//        String passValidated = docPassword.getText().toString().trim();
//        //Checks if the password field is correctly completed
//        if (passValidated.isEmpty()){                               //password field can not be empty
//            docPassword.setError("Field can not be empty");
//            return false;
//        } else if (!passPattern.matcher(passValidated).matches()) {     //password field must follow all the rules
//            docPassword.setError("Password is too weak");
//            return false;
//        }else{                                                      //password field is correctly completed
//            docPassword.setError(null);
//            return true;
//        }
//    }

    //DONE: Email validation
//    private boolean validateEmail(){
//        String email = "^[a-zA-Z0-9.%+-]"
//                + "+@[a-zA-Z0-9.-]"
//                + "+\\.[a-zA-Z]{2,}$";
//        Pattern emailPattern = Pattern.compile(email);
//
//        String emailValidated = docEmail.getText().toString().trim();
//
//        //Checks if the password field is correctly completed
//        if (emailValidated.isEmpty()){                                     //email field can not be empty
//            docEmail.setError("Field can not be empty");
//            return false;
//        } else if (!emailPattern.matcher(emailValidated).matches()) {     //email field must follow all the rules
//            docEmail.setError("Wrong email syntax! It must be example@domain.com");
//            return false;
//        }else{                                                            //email field is correctly completed
//            docEmail.setError(null);
//            return true;
//        }
//    }

    //DONE: Phone number validation
//    private boolean validatePhoneNumber(){
//        String phoneNumber = "^\\+[1-9\\d{1,10}$]";
//        Pattern phonePattern = Pattern.compile(phoneNumber);
//
//        String phoneNumberValidated = docPhoneNum.getText().toString().trim();
//
//        //Checks if the password field is correctly completed
//        if (phoneNumberValidated.isEmpty()){                                     //phone number field can not be empty
//            docPhoneNum.setError("Field can not be empty");
//            return false;
//        } else if (!phonePattern.matcher(phoneNumberValidated).matches()) {     //phone number field must follow all the rules
//            docPhoneNum.setError("Wrong phone number! It must contain only numbers," + "\n and the length it has to be 10");
//            return false;
//        }else{                                                                  //phone number field is correctly completed
//            docPhoneNum.setError(null);
//            return true;
//        }
//    }

    //DONE: Address validation
//    private boolean validateAddress(){
//        String myAddress = "^([A-Za-z\\s\\-']+\\s\\d+),"
//                + "\\s([A-Za-z\\s]+)\\s(\\d{5}),\\s([A-Za-z\\s]+)$";
//        Pattern addressPattern = Pattern.compile(myAddress);
//
//        String addressValidated = docAddress.getText().toString().trim();
//
//        //Checks if the password field is correctly completed
//        if (addressValidated.isEmpty()){                                      //address field can not be empty
//            docAddress.setError("Field can not be empty");
//            return false;
//        } else if (!addressPattern.matcher(addressValidated).matches()) {     //address field must follow all the rules
//            docAddress.setError("Wrong address syntax! It must be: Road No, Area Postal Code, City");
//            return false;
//        }else{                                                                 //address field is correctly completed
//            docAddress.setError(null);
//            return true;
//        }
//    }
}
