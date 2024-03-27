package com.wellnesswaveandroid.wellnesswaveandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //For testing puposes
//        setContentView(R.layout.activity_log_in);
//        setContentView(R.layout.activity_user_type);
//        setContentView(R.layout.activity_doc_sign_in);
//        setContentView(R.layout.activity_doc_details);
//        setContentView(R.layout.activity_pat_sign_in);
//        setContentView(R.layout.activity_pat_details);

        enter = findViewById(R.id.enterBtn);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent enterApplication = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(enterApplication);
            }
        });
    }
}