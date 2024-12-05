package com.wellnesswaveandroid.wellnesswaveandroid.Retrofit;

import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PatientApi {
    //DONE: POST ENDPOINT createPatient
    @POST("/createPatient")
    Call<Patient> createPatient(@Body Patient patient);

    //TODO: Get endpoint for getPatientById
    //DONE: logIn endpoint
    @FormUrlEncoded
    @POST("/patientLogInSession")
    Call<Result> logIn(@Field("username") String username, @Field("password") String password);

    @PATCH("/updatePatient/{id}")
    Call<Patient> updatePatient(@Path("id") Integer id, @Body Map<String, Object> updatedPatMap);

    @POST("/deletePatient/{id}")
    Call<Result> deletePatient(@Path("id") Integer patientId);
}
