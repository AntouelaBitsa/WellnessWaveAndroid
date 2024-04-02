package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wellnesswaveandroid.wellnesswaveandroid.R;

public class UserTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        //Calling sign in for user type : Doctor
        Button doctorUser = findViewById(R.id.doctorUserTypeBtn);
        doctorUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent doctorSignIn = new Intent(UserTypeActivity.this, DocSignInActivity.class);
                startActivity(doctorSignIn);
            }
        });


        //Calling sign in for user type : Patient
//        Button patientUser = findViewById(R.id.patientUserTypeBtn);
//        Intent patientSignIn = new Intent(UserTypeActivity.this, PatSignInActivity.class);
//        startActivity(patientSignIn);

    }
}