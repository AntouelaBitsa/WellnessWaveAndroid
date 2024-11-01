package com.wellnesswaveandroid.wellnesswaveandroid.Activities;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.AppointmentsApi;
import com.wellnesswaveandroid.wellnesswaveandroid.Retrofit.RetrofitService;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.PatAppointmentCarouselAdapter;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.SpecializationCarouselAdapter;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.SpecializationSet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientHomePageActivity extends AppCompatActivity {

    //Carousel of appointments elements
    private RecyclerView recyclerView;
    private PatAppointmentCarouselAdapter appointmentCarouselAdapter;
    private ArrayList<Appointments> appointmentsList;
    private Integer patientID;
    private TextView patUsername;
    private String pUsername;

    //Carousel of specializations elements
    private RecyclerView specializationRecyclerView;
    private SpecializationCarouselAdapter specializationCarouselAdapter;
    private List<SpecializationSet> specialization;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home_page);

        bottomNavigationView = findViewById(R.id.bottomNavBar);
        //Carousel for Appointments
        patUsername = findViewById(R.id.usernamePatTxt);
        recyclerView = findViewById(R.id.appointmentRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //Getting the patients id with singleton pattern
        Patient pat = Patient.getInstance();
        if (pat != null) {
            System.out.println("[-SOS-]: pat Id inside if: " + pat.getPatientId());
            patientID = pat.getPatientId();
            pUsername = pat.getPatUsername();
        }
        System.out.println("[-**0-PatientHomePageActivity] onCreate: patientID= " + patientID);
        appointmentsList = new ArrayList<>();
        appointmentCarouselAdapter = new PatAppointmentCarouselAdapter(appointmentsList, this);
        recyclerView.setAdapter(appointmentCarouselAdapter);
        //GET Request Implementation : getting a list of appointments based on the id of the user
        getAppointmentsInfo(patientID);

        //Carousel for Specialization
        specialization = getSetsOfSpecTitleAndIcon();

        specializationRecyclerView = findViewById(R.id.docSpecializationRecyclerView);
        specializationRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        specializationCarouselAdapter = new SpecializationCarouselAdapter(specialization, this);
        specializationRecyclerView.setAdapter(specializationCarouselAdapter);
    }

    private List<SpecializationSet> getSetsOfSpecTitleAndIcon() {
        String[] specTitle = getResources().getStringArray(R.array.specialisations_array);
        TypedArray specIcons = getResources().obtainTypedArray(R.array.specialization_icons_set);
        List<SpecializationSet> specSetList = new ArrayList<>();
        for (int i=0; i<specTitle.length; i++){
            specSetList.add(new SpecializationSet(specTitle[i], specIcons.getDrawable(i)));
        }
        specIcons.recycle();
        return specSetList;
    }

    private void getAppointmentsInfo(Integer pID) {
        //TODO: Maybe it will be needed Singleton Pattern
        //TODO: call get request of get list of appointments of a specific user
        RetrofitService retrofitService = new RetrofitService();
        AppointmentsApi appointmentsApi = retrofitService.getRetrofit().create(AppointmentsApi.class);
        appointmentsApi.getAppointmentsByPatient(pID).enqueue(new Callback<List<Appointments>>() {
            @Override
            public void onResponse(Call<List<Appointments>> call, Response<List<Appointments>> response) {
                System.out.println("[-0-PatientHomePageActivity] onResponse: response BODY-> " + response.body());
                //TODO: set those data to the appointmentsList
                if (!response.isSuccessful() || response.body() == null) {
                    System.out.println("[-1-PatientHomePageActivity] onResponse: successful= no + body== null");
                    Toast.makeText(PatientHomePageActivity.this, "Failed to get patient's appointments list", Toast.LENGTH_SHORT).show();
                }

                System.out.println("[-2-PatientHomePageActivity] onResponse: response BODY2--> " + response.body());
                appointmentsList.addAll(response.body());
                patUsername.setText(pUsername);
                if (appointmentsList.isEmpty()) {
                    System.out.println("[-3-PatientHomePageActivity] onResponse: the appointmentsList is empty");
                }
                appointmentCarouselAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Appointments>> call, Throwable throwable) {
                System.out.println("[-1-PatientHomePageActivity] onFailure: NOT OK");
                System.out.println("[-2-PatientHomePageActivity] onFailure - throwable.getMessage() : " + throwable.getMessage());
                Toast.makeText(PatientHomePageActivity.this, "Failed to fetch request" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}