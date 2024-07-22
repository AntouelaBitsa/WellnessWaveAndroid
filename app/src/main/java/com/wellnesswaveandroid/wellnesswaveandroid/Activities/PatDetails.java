package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;

public class PatDetails extends AppCompatActivity {
    private TextView firstNameTxt, lastNameTxt, usernameTxt, passwordTxt, emailTxt, amkaTxt, phoneNumTxt, dobTxt;
    private RetrofitService retrofitService;
    private Button btnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pat_details);

        btnTest = findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatDetails.this,BookAppointmentActivity.class);
                startActivity(intent);
            }
        });

        //initialization: find views by id for edit texts
        firstNameTxt = findViewById(R.id.patFNameTxt);
        lastNameTxt = findViewById(R.id.patLNameTxt);
        usernameTxt = findViewById(R.id.patUsernameTxt);
        passwordTxt = findViewById(R.id.patPasswordTxt);
        emailTxt = findViewById(R.id.patEmailTxt);
        amkaTxt = findViewById(R.id.patAmkaTxt);
        phoneNumTxt = findViewById(R.id.patPhoneNumTxt);
        dobTxt = findViewById(R.id.patDobTxt);

        Log.d("[P] Are HEre", " Yes");
        Toast.makeText(PatDetails.this, "Intent worked Successfully", Toast.LENGTH_LONG).show();

        Intent importPatData = getIntent();
        String firstName = importPatData.getStringExtra("first_namep");
        String lastName = importPatData.getStringExtra("last_namep");
        String username = importPatData.getStringExtra("usernamep");
        String email = importPatData.getStringExtra("emailp");
        String phoneNum = importPatData.getStringExtra("phonep");
        String dob = importPatData.getStringExtra("dobp");

        System.out.println("Intent Print : " + firstName+lastName+username+email+phoneNum+dob);

        firstNameTxt.setText(firstName);
        lastNameTxt.setText(lastName);
        usernameTxt.setText(username);
        emailTxt.setText(email);
        phoneNumTxt.setText(phoneNum);
        dobTxt.setText(dob);

    }
}