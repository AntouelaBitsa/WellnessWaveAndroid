package com.wellnesswaveandroid.wellnesswaveandroid.Retrofit;

import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PatientApi {
    //DONE: POST ENDPOINT createPatient
    @POST("/createPatient")
    Call<Patient> createPatient(@Body Patient patient);

    //TODO: Get andpoint for getPatientById
    //TODO: logIn endpoint
    @FormUrlEncoded
    @POST("/patientLogInSession")
    Call<Result> logIn(@Field("username") String username, @Field("password") String password);
}
