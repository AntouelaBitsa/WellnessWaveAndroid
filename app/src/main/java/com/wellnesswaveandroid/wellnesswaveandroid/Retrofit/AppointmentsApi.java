package com.wellnesswaveandroid.wellnesswaveandroid.Retrofit;

import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
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

    @GET("/getAppointmentsByPatient/{patientId}")
    Call<List<Appointments>> getAppointmentsByPatient(@Path("patientId") Integer patientId);

    @GET("/getAppointmentsByDoctor/{doctorId}")
    Call<List<Appointments>> getAppointmentsByDoctor(@Path("doctorId") Integer doctorId);

    //DONE: must double check name from backend
    @FormUrlEncoded
    @POST("/deleteAppointment")
    Call<Result> deleteAppointment(@Field("id") Integer appointId);

    @GET("/getAppointmentsOnDateSelected/{date}/{userId}/{userType}")
    Call<List<Appointments>> getAppointmentsOnDateSelected(@Path("date") String date, @Path("userId") Integer doctorId, @Path("userType") Integer userType);
}
