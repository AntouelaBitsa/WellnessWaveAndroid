package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.wellnesswaveandroid.wellnesswaveandroid.R;

public class UserTypeActivity extends AppCompatActivity {

    private Button doctorUser, patientUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        //Calling sign in for user type : Doctor
        doctorUser = findViewById(R.id.doctorUserTypeBtn);
        Intent doctorSignIn = new Intent(UserTypeActivity.this, DocSignInActivity.class);
        startActivity(doctorSignIn);

        //Calling sign in for user type : Patient
        patientUser = findViewById(R.id.patientUserTypeBtn);
        Intent patientSignIn = new Intent(UserTypeActivity.this, PatSignInActivity.class);
        startActivity(patientSignIn);

    }
}