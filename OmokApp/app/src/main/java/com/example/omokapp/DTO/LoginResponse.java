package com.example.omokapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class LoginResponse {
    private String message;
    private AccessToken accessToken;
    private RefreshToken refreshToken;
    public LoginResponse(){}
}
