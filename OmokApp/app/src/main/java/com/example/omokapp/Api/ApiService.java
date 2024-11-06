package com.example.omokapp.Api;

import com.example.omokapp.DTO.AIRequest;
import com.example.omokapp.DTO.AddOmokDataRequest;
import com.example.omokapp.DTO.PostGameDataRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/add_omok_data")
    Call<Void> addOmokData(@Body AddOmokDataRequest request);

    @POST("/api/data/post_game_data")
    Call<Void> postGameData(@Body PostGameDataRequest request);

    @POST("/api/ai_put")
    Call<Integer> aiPut(@Body AIRequest request);
}
