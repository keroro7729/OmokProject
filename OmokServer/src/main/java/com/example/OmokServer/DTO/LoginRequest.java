package com.example.OmokServer.DTO;

import com.example.OmokServer.Secure.DeviceInfo;
import com.example.OmokServer.Secure.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class LoginRequest {
    private Long userId;
    private String username, password;
    private DeviceInfo deviceInfo;
    public LoginRequest(){}
}
