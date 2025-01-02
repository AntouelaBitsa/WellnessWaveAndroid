package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Diagnosis;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.DiagnosisApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.CustomZoomLayoutManager;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.DiagnosisRecordAdapter;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.PDFGenerator;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.SpecializationCarouselAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiagnosisRecordsActivity extends AppCompatActivity {

    private RecyclerView diagnosisHistoryRecyclerView;
    private DiagnosisRecordAdapter diagnosisRecordAdapter;
    private Dialog diagnosisDetailsPopUp;
    private TextView diagnType, diagnInstractions, treatmentName, treatmentDose;
    private Button convertToPDFbtn;
    private List<Diagnosis> diagnosisList;
    private Patient patInstance;
    private PDFGenerator pdfGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diagnosis_records);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        patInstance = Patient.getInstance();
        if (patInstance == null){
            System.out.println("[1 DiagnHistory] Patient's Instance is NULL");
            Log.d("DiagnosisRecordsActivity", "(1) onCreate: Patient instance --> NULL");
            return;
        }
        System.out.println("[2 DiagnHistory] Patient's Instance is: " + patInstance.toString());
        Log.d("DiagnosisRecordsActivity", "(2) onCreate: Patient instance --> " + patInstance.toString());

        diagnosisList = new ArrayList<>();
        getDiagnosisOfPatient(patInstance.getPatientId());

        diagnosisHistoryRecyclerView = findViewById(R.id.specRecyclerRecyclerView);
        diagnosisHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //TODO: Call get Request of Get All Diagnosis
        diagnosisRecordAdapter = new DiagnosisRecordAdapter(diagnosisList, this, new DiagnosisRecordAdapter.OnBtnListener() {
            @Override
            public void onDiagnosisButtonClick(Diagnosis diagnosis) {
                //TODO: callPopUpDialog(diagnosis);
                callDiagnosisDetailsPopUp(diagnosis);
                //TODO: popUp.show();
                diagnosisDetailsPopUp.show();
            }
        });
        diagnosisHistoryRecyclerView.setAdapter(diagnosisRecordAdapter);
    }

    private void callDiagnosisDetailsPopUp(Diagnosis diagnosis) {
        diagnosisDetailsPopUp = new Dialog(DiagnosisRecordsActivity.this);
        diagnosisDetailsPopUp.setContentView(R.layout.manage_selected_appointment_popup_dialog);
        diagnosisDetailsPopUp.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        diagnosisDetailsPopUp.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_popup_background));
        diagnosisDetailsPopUp.setCancelable(true);//when user clicks outside the dialog can not close

        //TODO: Declare Components for TextViews
        diagnType = diagnosisDetailsPopUp.findViewById(R.id.diagnTypeCardTxt);
        diagnInstractions = diagnosisDetailsPopUp.findViewById(R.id.diagnInstructionsCardTxt);
        treatmentName = diagnosisDetailsPopUp.findViewById(R.id.treatNameCardTxt);
        treatmentDose = diagnosisDetailsPopUp.findViewById(R.id.treatDoseCardTxt);


        //TODO: set context to TextViews
        diagnType.setText(diagnosis.getDiagnType());
        diagnInstractions.setText(diagnosis.getDiagnInfo());
        treatmentName.setText(diagnosis.getTreatment());
        treatmentDose.setText(diagnosis.getTreatmDose());

        convertToPDFbtn = diagnosisDetailsPopUp.findViewById(R.id.convertToPDFbtn);

        convertToPDFbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfGenerator = new PDFGenerator(DiagnosisRecordsActivity.this);
                pdfGenerator.generatePDF(diagnosis.getPatient(), diagnosis.getDoctor(), diagnosis);
                Toast.makeText(DiagnosisRecordsActivity.this, "PDF File Generated Successfully", Toast.LENGTH_SHORT).show();
                diagnosisDetailsPopUp.dismiss();
            }
        });
    }

    private void getDiagnosisOfPatient(Integer patientId) {
        RetrofitService retrofitService = new RetrofitService();
        DiagnosisApi diagnosisApi = retrofitService.getRetrofit().create(DiagnosisApi.class);
        diagnosisApi.getListOfDiagnosisByPatient(patientId).enqueue(new Callback<List<Diagnosis>>() {
            @Override
            public void onResponse(Call<List<Diagnosis>> call, Response<List<Diagnosis>> response) {
                System.out.println("[DiagnosisRecordsActivity] onResponse 1 : response BODY -> " + response.body());
                diagnosisList.clear();
                if (!response.isSuccessful() || response.body() == null) {
                    System.out.println("[DiagnosisRecordsActivity] onResponse 2 : isSuccessful() => no + body() => null");
                    Toast.makeText(DiagnosisRecordsActivity.this, "Failed to get doctor's diagnosis list", Toast.LENGTH_SHORT).show();
                    return;
                }

                System.out.println("[DiagnosisRecordsActivity] onResponse 3 : response BODY 2 --> " + response.body());
                diagnosisList.addAll(response.body());
                //patUsername.setText(pUsername);
                if (diagnosisList.isEmpty()) {
                    System.out.println("[DiagnosisRecordsActivity] onResponse 4 : the diagnosisList is empty");
                }
                System.out.println("[DiagnosisRecordsActivity] LIST SIZE IN RESPONSE: " + diagnosisList.size());
                diagnosisRecordAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Diagnosis>> call, Throwable throwable) {
                System.out.println("[DiagnosisRecordsActivity] onFailure: NOT OK");
                System.out.println("[DiagnosisRecordsActivity] onFailure - throwable.getMessage() : " + throwable.getMessage());
                Toast.makeText(DiagnosisRecordsActivity.this, "Failed to fetch request" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}