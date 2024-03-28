package com.wellnesswaveandroid.wellnesswaveandroid.Retrofit;

import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DoctorApi {
    @POST("/createDoctor")
    Call<Doctor> createDoctor(@Body Doctor doctor);
}
