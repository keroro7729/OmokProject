package com.example.omokapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class LoginRequest {
    private Long userId;
    private String username, password;
    private DeviceInfo deviceInfo;
    public LoginRequest(){}
}

