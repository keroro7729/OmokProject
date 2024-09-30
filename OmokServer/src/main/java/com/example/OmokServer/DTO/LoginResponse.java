package com.example.OmokServer.DTO;

import com.example.OmokServer.Secure.AccessToken;
import com.example.OmokServer.Secure.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class LoginResponse {
    private String message;
    private AccessToken accessToken;
    private RefreshToken refreshToken;
    public LoginResponse(){}
}
