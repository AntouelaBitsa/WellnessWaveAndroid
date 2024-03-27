package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wellnesswaveandroid.wellnesswaveandroid.R;

public class LogInActivity extends AppCompatActivity {

    private Button logInButton, signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //TODO : implementation Log In Button

        signInButton = findViewById(R.id.signInBtn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userType = new Intent(LogInActivity.this, UserTypeActivity.class);
                startActivity(userType);
            }
        });
    }
}