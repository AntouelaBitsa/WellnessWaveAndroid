package com.wellnesswaveandroid.wellnesswaveandroid.Retrofit;

import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DoctorApi {
    @POST("/createDoctor")
    Call<Doctor> createDoctor(@Body Doctor doctor);

    //DONE: get endpoint statement
    @GET("/getDoctorById/{id}")
    Call<Doctor> getDoctorById(@Path("id") Integer id);

    //DONE : post endpoint for LogIN -> MAYBE NOT NEEDED
    @FormUrlEncoded
    @POST("/doctorLogInSession")
    Call<Result> logIN(@Field("username") String username, @Field("password") String password);
}
