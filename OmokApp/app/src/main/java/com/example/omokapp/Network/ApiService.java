package com.example.omokapp.Network;

import com.example.omokapp.DTO.AccessToken;
import com.example.omokapp.DTO.LoginAttemptResponse;
import com.example.omokapp.DTO.LoginRequest;
import com.example.omokapp.DTO.LoginResponse;
import com.example.omokapp.DTO.RefreshToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/api/users/register")
    Call<String> register(@Query("username") String username, @Query("password") String password);

    @POST("/api/users/login-attempt")
    Call<LoginAttemptResponse> loginAttempt(@Query("username") String username);

    @POST("/api/users/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/api/users/refresh-access-token")
    Call<AccessToken> refreshAccessToken(@Body RefreshToken token);
}
