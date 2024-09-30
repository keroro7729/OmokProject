package com.example.OmokServer.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.crypto.SecretKey;

@Getter @Setter
public class LoginAttemptResponse {
    private String keyData;
    private Long userId;
}
