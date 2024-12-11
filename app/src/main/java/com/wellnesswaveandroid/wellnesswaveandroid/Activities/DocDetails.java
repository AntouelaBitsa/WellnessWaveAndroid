package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;

import java.util.Map;

public class DocDetails extends AppCompatActivity {
    //DONE: data arent showing into my Activity must check for it
    private BottomNavigationView bottomNavigationView;
    TextView fullNameTxt, lastNameTxt, usernameTxt;
    private TextInputEditText passwordTxt, emailTxt, amkaTxt, phoneNumTxt, professionTxt, addressTxt;
    private ImageView deleteDocAccount;
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
        //TODO: DELETE PROFILE LISTENER & CALL REQUEST
    }

}