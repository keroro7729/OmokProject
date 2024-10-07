package com.example.omokapp.Api;

import android.util.Log;

import com.example.omokapp.DTO.AddOmokDataRequest;
import com.example.omokapp.DTO.OmokData;
import com.example.omokapp.Enums.GameState;
import com.example.omokapp.SingleTons.Storage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private Retrofit retrofit;
    private ApiService service;
    private Storage storage;
    private static final String TAG = "ApiManager";
    private static final String OMOK_DATA = "omok_data";

    public ApiManager(String baseUrl){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);
        storage = Storage.getInstance();
    }
    public ApiService getService(){ return service; }

    public void sendGameHistory(String history, GameState state){
        String dataFile = storage.get(OMOK_DATA);
        OmokData data = new OmokData(state.getCode(), history);
        if(dataFile == null)
            dataFile = data.toString();
        else dataFile += "\n"+data;
        AddOmokDataRequest request = new AddOmokDataRequest(dataFile);

        Call<Void> call = service.addOmokData(request);
        final String payload = dataFile;
        Log.d(TAG, "omok data payload: "+payload);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    storage.remove(OMOK_DATA);
                }
                else{
                    Log.e(TAG, "sendGameHistory failed");
                    Log.e(TAG, "request: "+call.request());
                    Log.e(TAG, "http code: "+response.code());
                    storage.set(OMOK_DATA, payload);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                storage.set(OMOK_DATA, payload);
            }
        });
    }
}
