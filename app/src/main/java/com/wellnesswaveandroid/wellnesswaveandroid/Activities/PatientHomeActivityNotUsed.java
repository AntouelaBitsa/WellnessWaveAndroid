package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wellnesswaveandroid.wellnesswaveandroid.R;

public class PatientHomeActivityNotUsed extends AppCompatActivity {

    private Button appointBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home_not_used);

        appointBtn = findViewById(R.id.patBookAppoint);
        appointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookAppointIntent = new Intent(PatientHomeActivityNotUsed.this, BookAppointmentActivity.class);
                startActivity(bookAppointIntent);

            }
        });

    }
}