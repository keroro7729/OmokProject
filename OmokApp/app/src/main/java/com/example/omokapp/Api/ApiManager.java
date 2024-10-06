package com.example.omokapp.Api;

import com.example.omokapp.Enums.GameState;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private Retrofit retrofit;
    private ApiService service;
    public ApiManager(String baseUrl){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);
    }
    public ApiService getService(){ return service; }

    public void sendGameHistory(List<Integer> history, GameState state){

    }
}
