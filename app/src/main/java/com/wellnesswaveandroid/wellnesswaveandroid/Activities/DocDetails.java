package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.DoctorApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocDetails extends AppCompatActivity {
    //TODO: data arent showing into my Activity must check for it
    TextView firstNameTxt, lastNameTxt, usernameTxt, passwordTxt, emailTxt, amkaTxt, phoneNumTxt, professionTxt, addressTxt;
    RetrofitService retrofitService;
    private Button btnTest;
    private static Integer docID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_details);

        //initialization: find views by id for edit texts
        firstNameTxt = findViewById(R.id.docFNameTxt);
        lastNameTxt = findViewById(R.id.docLNameTxt);
        usernameTxt = findViewById(R.id.docUsernameTxt);
        passwordTxt = findViewById(R.id.docPasswordTxt);
        emailTxt = findViewById(R.id.docEmailTxt);
        amkaTxt = findViewById(R.id.docAmkaTxt);
        phoneNumTxt = findViewById(R.id.docPhoneNumTxt);
        professionTxt = findViewById(R.id.docProfessionTxt);
        addressTxt = findViewById(R.id.docAddressTxt);

        Log.d("[D] Are HEre", " Yes");
        Toast.makeText(DocDetails.this, "Intent worked Successfully", Toast.LENGTH_LONG).show();

        Intent importedDocData = getIntent();
        docID = importedDocData.getIntExtra("doc_id",-1); //test
        String firstName = importedDocData.getStringExtra("first_name");
        String lastName = importedDocData.getStringExtra("last_name");
        String username = importedDocData.getStringExtra("username");
        String email = importedDocData.getStringExtra("email");
        String phoneNum = importedDocData.getStringExtra("phone");
        String profession = importedDocData.getStringExtra("prof");
        String address = importedDocData.getStringExtra("address");

//        Doctor d = Doctor.getInstance();
//        String firstName = d.getDocFirstName();
//        String lastName = d.getDocLastName();
//        String username = d.getDocUsername();
//        String email = d.getDocEmail();
//        String phoneNum = d.getDocPhoneNum();
//        String profession = d.getDocProfession();
//        String address = d.getDocAddress();

        Doctor.getInstance().setDocId(docID);  // Ensure the ID is set
        Doctor.getInstance().setDocFirstName(firstName);
        Doctor.getInstance().setDocLastName(lastName);
        Doctor.getInstance().setDocUsername(username);
        Doctor.getInstance().setDocEmail(email);
        Doctor.getInstance().setDocPhoneNum(phoneNum);
        Doctor.getInstance().setDocProfession(profession);
        Doctor.getInstance().setDocAddress(address);

        System.out.println("Intent Print : " + firstName+ " " +lastName+ " " +username+ " " +email+
                " " +phoneNum+ " " +profession+ " " +address);

        firstNameTxt.setText(firstName);
        lastNameTxt.setText(lastName);
        usernameTxt.setText(username);
        emailTxt.setText(email);
        phoneNumTxt.setText(phoneNum);
        professionTxt.setText(profession);
        addressTxt.setText(address);

        //TEST BUTTON
        btnTest = findViewById(R.id.btnTestDoc);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DocDetails.this, InsertDiagnosisActivity.class);
//                intent.putExtra("doc_id", docID);
                startActivity(intent);
            }
        });

//        callGetRequestForDetails();
        //TEST INTENT FROM MAIN
//        intentGetMethod("Antouela");
    }

    private Integer returnDocID(Integer id){
        return docID;
    }
}