package com.example.omokapp.Api;

import com.example.omokapp.DTO.AddOmokDataRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/add_omok_data")
    Call<Void> addOmokData(@Body AddOmokDataRequest request);
}
