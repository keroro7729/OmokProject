package com.example.omokapp.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginAttemptResponse {
    private String keyData;
    private Long userId;
}

