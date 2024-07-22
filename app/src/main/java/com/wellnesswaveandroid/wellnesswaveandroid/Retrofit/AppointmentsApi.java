package com.wellnesswaveandroid.wellnesswaveandroid.Retrofit;

import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AppointmentsApi {
    @FormUrlEncoded
    @POST("/getSpecialisedDoc")
    Call<Result> getSpecialisedDoc(@Field("profession") String profession);

    @POST("/createAppointments")
    Call<Result> createAppointments(@Body Appointments appointment);
}
