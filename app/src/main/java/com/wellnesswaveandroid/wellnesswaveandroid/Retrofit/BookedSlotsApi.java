package com.wellnesswaveandroid.wellnesswaveandroid.Retrofit;

import retrofit2.Call;

import com.wellnesswaveandroid.wellnesswaveandroid.Entities.BookedSlots;
import com.wellnesswaveandroid.wellnesswaveandroid.Utils.Result;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookedSlotsApi {

    @GET("/getBookedSlotsByDate/{date}/{docId}")
    Call<Map<String, Set<String>>> getBookedSlotsByDate(@Path("date") String date, @Path("docId") Integer docId);

    @POST("/createSlot/{docId}")
    Call<Result> createSlot(@Body BookedSlots bookedSlots, @Path("docId") Integer docId);

    @PUT("/updateSlot/{slotDate}/{docId}")
    Call<Result> updateSlot(
            @Path("slotDate") String slotDate,        // Path variable for slotDate
            @Path("docId") Integer docId,             // Path variable for docId
            @Query("newSlotTime") String newSlotTime  // Request parameter (query parameter)
    );
}
