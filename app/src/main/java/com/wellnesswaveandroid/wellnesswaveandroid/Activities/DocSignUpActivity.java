package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
    private Spinner docSpecializationsSpinner;
    private String specialization;

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
//        docProfession = findViewById(R.id.docProfessionEdt);
        docSpecializationsSpinner = findViewById(R.id.docProfessionSpn);
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

        //DONE : add contents to spinner specialisation
        System.out.println("Adding data to specialisation spinner");
        Log.d("TAG 1 ", "Adding data to specialisation spinner");
        //Adding data to specialisation spinner from xml file - Static Data
        String[] spec = getResources().getStringArray(R.array.specialisations_array);
        ArrayAdapter<String> specialisationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spec);
        specialisationAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item_layout);
        docSpecializationsSpinner.setAdapter(specialisationAdapter);
        System.out.println("Implementation: on item selected Of spinner");
        Log.d("TAG 2 ", "Implementation: on item selected Of spinner");
        /**
         * Get Specialization Selection from Drop Down
         */
        docSpecializationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("TAG 3 ", "Inside on item selected method");
                specialization = spec[position];
                System.out.println("specialisation onClick => " + specialization);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });



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
//                String professionErrorMessage = fieldsValidators.validateProfession(docProfession.getText().toString().trim());
//                professionErrMessageTxt.setVisibility(View.VISIBLE);
//                professionErrMessageTxt.setText(professionErrorMessage);
//                professionErrMessageTxt.setTextColor(professionErrMessageTxt.getContext().getColor(R.color.errorred));

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
                        docAddress.getText().toString().trim());
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
                String profession = specialization;
                String address = String.valueOf(docAddress.getText());
                Integer type = 1; //code for doc

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

}
