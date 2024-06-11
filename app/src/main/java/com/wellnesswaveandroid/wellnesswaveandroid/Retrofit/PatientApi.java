package com.wellnesswaveandroid.wellnesswaveandroid.Retrofit;

import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PatientApi {
    //DONE: POST ENDPOINT createPatient
    @POST("/createPatient")
    Call<Patient> createPatient(@Body Patient patient);

    //TODO: Get andpoint for getPatientById
    //TODO: logIn endpoint
}
