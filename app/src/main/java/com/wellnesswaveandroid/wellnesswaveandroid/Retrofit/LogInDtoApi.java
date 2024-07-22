package com.wellnesswaveandroid.wellnesswaveandroid.Retrofit;

import com.wellnesswaveandroid.wellnesswaveandroid.Utils.LogInDTO;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LogInDtoApi {
    @POST("/logIN")
    Call<Result> logIN(@Body LogInDTO logInDTO);
}
