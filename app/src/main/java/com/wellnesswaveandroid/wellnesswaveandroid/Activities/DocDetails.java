package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.DoctorApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.PatientApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocDetails extends AppCompatActivity {
    //DONE: data arent showing into my Activity must check for it
    private BottomNavigationView bottomNavigationView;
    private TextView fullNameTxt, lastNameTxt, usernameTxt;
    private TextInputEditText passwordTxt, emailTxt, amkaTxt, phoneNumTxt, professionTxt, addressTxt;
    //TODO: Delete account
    private ImageView deleteDocAccount;
    //TODO: Update Doctors Information
    private Button editDocProfile;
    RetrofitService retrofitService;
    private static Integer docID;
    private Doctor docInstance;
    private boolean btnState = false;
    private Map<String, Object> updatedFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_details);

        bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(getApplicationContext(), DoctorHomePageActivity.class));
                    //if there are transitions=> overridePendingTransition()
                    finish();
                    return true;
                }else if (item.getItemId() == R.id.nav_profile) {
                    return true;
                } else if (item.getItemId() == R.id.nav_manage_appointments) {
//                        startActivity(new Intent(getApplicationContext() /*, Manage Appointments class */));
                    //if there are transitions=> overridePendingTransition()
//                        finish();
                    return true;
                }else {
                    return false;
                }
            }
        });

        //initialization: find views by id for edit texts
        fullNameTxt = findViewById(R.id.docFullNameTxt);
        usernameTxt = findViewById(R.id.docUsernameTxt);
//        passwordTxt = findViewById(R.id.docPasswordTxt);
        emailTxt = findViewById(R.id.docEmailTxt);
//        amkaTxt = findViewById(R.id.docAmkaTxt);
        phoneNumTxt = findViewById(R.id.docPhoneNumTxt);
        professionTxt = findViewById(R.id.docProfessionTxt);
        addressTxt = findViewById(R.id.docAddressTxt);
        editDocProfile = findViewById(R.id.editDocProfileBtn);
        deleteDocAccount = findViewById(R.id.deleteDocAccount);

        Log.d("[D] Are HEre", " Yes");
        Toast.makeText(DocDetails.this, "Intent worked Successfully", Toast.LENGTH_LONG).show();

        Intent importedDocData = getIntent();
        docID = importedDocData.getIntExtra("doc_id",-1); //test
        String fullName = importedDocData.getStringExtra("first_name");
        String lastName = importedDocData.getStringExtra("last_name");
        String username = importedDocData.getStringExtra("username");
        String email = importedDocData.getStringExtra("email");
        String phoneNum = importedDocData.getStringExtra("phone");
        String profession = importedDocData.getStringExtra("prof");
        String address = importedDocData.getStringExtra("address");

//        Doctor d = Doctor.getInstance();
//        String fullName = d.getDocFirstName();
//        String lastName = d.getDocLastName();
//        String username = d.getDocUsername();
//        String email = d.getDocEmail();
//        String phoneNum = d.getDocPhoneNum();
//        String profession = d.getDocProfession();
//        String address = d.getDocAddress();

//        Doctor.getInstance().setDocId(docID);  // Ensure the ID is set
//        Doctor.getInstance().setDocFirstName(fullName);
//        Doctor.getInstance().setDocLastName(lastName);
//        Doctor.getInstance().setDocUsername(username);
//        Doctor.getInstance().setDocEmail(email);
//        Doctor.getInstance().setDocPhoneNum(phoneNum);
//        Doctor.getInstance().setDocProfession(profession);
//        Doctor.getInstance().setDocAddress(address);

        docInstance = Doctor.getInstance();
        if (docInstance == null){
            System.out.println("The doctor instance in DOCDETAILS Class is null");
            return;
        }
        String dr = "Dr.";
        fullName = dr.concat(" ").concat(docInstance.getDocFirstName()).concat(" ").concat(docInstance.getDocLastName());
//        lastName = docInstance.getDocLastName();
        username = docInstance.getDocUsername();
        email = docInstance.getDocEmail();
        phoneNum = docInstance.getDocPhoneNum();
        profession = docInstance.getDocProfession();
        address = docInstance.getDocAddress();

        System.out.println("Intent Print : " + fullName+ " " +lastName+ " " +username+ " " +email+
                " " +phoneNum+ " " +profession+ " " +address);

        fullNameTxt.setText(fullName);
//        lastNameTxt.setText(lastName);
        usernameTxt.setText(username);
        emailTxt.setText(email);
        phoneNumTxt.setText(phoneNum);
        professionTxt.setText(profession);
        addressTxt.setText(address);

        //TODO: EDIT PROFILE LISTENER & CALL REQUEST
        editDocProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnState){
                    emailTxt.setEnabled(true);
                    phoneNumTxt.setEnabled(true);
                    addressTxt.setEnabled(true);

                    editDocProfile.setText("Save Changes");
                    btnState = true;
                    System.out.println("[DEBUG NEW] Edit profile text: changed, btnState: true, MaterialEditText: enabled");
                }else {
                    String updatedEmail = emailTxt.getText().toString();
                    String updatedPhoneNum = phoneNumTxt.getText().toString();
                    String updatedAddress = addressTxt.getText().toString();
                    System.out.println("[DEBUG NEW 1] Updated => Email: " + updatedEmail + " PhoneNum: " + updatedPhoneNum +
                            " BirthDate: " + updatedAddress);

                    docInstance.setDocEmail(updatedEmail);
                    docInstance.setDocPhoneNum(updatedPhoneNum);
                    docInstance.setDocAddress(updatedAddress);

                    System.out.println("DEBUG NEW 2] Updated Patient Object: " + docInstance.toString());

                    System.out.println("[DEBUG NEW 3] Updated => PatientId: " + docInstance.getDocId() +
                            " Full Name: " + docInstance.getDocFirstName() + " " + docInstance.getDocLastName() +
                            " Username: " + docInstance.getDocUsername() +
                            " Email: " + docInstance.getDocEmail() +
                            " Phone Num: " + docInstance.getDocPhoneNum() +
                            " Address: " + docInstance.getDocAddress());


                    updatedFields = new HashMap<>();
                    updatedFields.put("email", docInstance.getDocEmail());
                    updatedFields.put("phoneNum", docInstance.getDocPhoneNum());
                    updatedFields.put("address", docInstance.getDocAddress());

                    updateDoctor(docInstance.getDocId(), updatedFields);

                    emailTxt.setEnabled(false);
                    phoneNumTxt.setEnabled(false);
                    addressTxt.setEnabled(false);

                    editDocProfile.setText("Edit Profile");
                    btnState = false;
                }

            }
        });
        //DONE: DELETE PROFILE LISTENER & CALL REQUEST
        deleteDocAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DONE: Delete API Request
                deleteDoctor(docInstance.getDocId());
            }
        });
    }

    private void updateDoctor(Integer docId, Map<String, Object> updatedFields) {
        System.out.println("[Pat 2] Inside updatePatient() before retrofit initialization -> patient = " + updatedFields);
        RetrofitService retrofitDocService = new RetrofitService();
        System.out.println("[Pat 2.1] API initialization = ");
        DoctorApi doctorApi = retrofitDocService.getRetrofit().create(DoctorApi.class);
        doctorApi.updateDoctor(docId, updatedFields).enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (response.isSuccessful()){
                    Doctor responseDoc = response.body();
                    if (responseDoc != null){
                        Log.d("Patient Update", "Updated Patient: " + responseDoc.toString());
                    }
                }else {
                    // Handle the error
                    Log.e("Patient Update", "Failed to update patient. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {
                Toast.makeText(DocDetails.this, "Request Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("[ 2] TAG: " + " onFailure: ", "The Message is : " + t.getMessage());
            }
        });
    }

    private void deleteDoctor(Integer docId) {
        System.out.println("[Pat 1] Inside deletePatient() before retrofit initialization -> patient ID = " + docId);
        RetrofitService retrofitDocService = new RetrofitService();
        System.out.println("[Pat 1.1] API initialization = ");
        DoctorApi doctorApi = retrofitDocService.getRetrofit().create(DoctorApi.class);
        doctorApi.deleteDoctor(docId).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.d("[onResponse] Doctor DELETE", "onResponse Triggered. Response code: " + response.code());
                if (response.isSuccessful()){
                    Toast.makeText(DocDetails.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("Doctor DELETE", "Doctor has been deleted Successfully");
                    Intent goToWelcomeScreen = new Intent(DocDetails.this, MainActivity.class);
                    goToWelcomeScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(goToWelcomeScreen);
                    finish();
//                    runOnUiThread(() -> {
//                        Intent goToWelcomeScreen = new Intent(DocDetails.this, MainActivity.class);
//                        goToWelcomeScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(goToWelcomeScreen);
//                        finish();
//                        Log.d("Navigation", "Navigation to MainActivity should have occurred");
//                    });
                    return;
                }else {
                    // Handle the error
                    Toast.makeText(DocDetails.this, response.body() +" & " + response.code(), Toast.LENGTH_LONG).show();
                    Log.e("Doctor DELETE", "Failed to DELETE Doctor. Response code: " + response.code() + " & " + response.body());
                    return;
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(DocDetails.this, "Request Failed" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("[ 2] TAG: " + " onFailure: ", "The Message is : " + t.getMessage());
            }
        });
    }

}