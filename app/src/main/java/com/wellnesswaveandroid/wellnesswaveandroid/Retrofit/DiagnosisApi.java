package com.wellnesswaveandroid.wellnesswaveandroid.Retrofit;

import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Appointments;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Diagnosis;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DiagnosisApi {
    @GET("/searchPatByAmka/{patAmka}")
    Call<Patient> searchPatByAmka(@Path("patAmka") String patAmka);

    @POST("/createDiagnosis")
    Call<Result> createDiagnosis(@Body Diagnosis diagnosis);

    @GET("/getListOfDiagnosisByPatient/{patId}")
    Call<List<Diagnosis>> getListOfDiagnosisByPatient(@Path("patId") Integer patId);
}
