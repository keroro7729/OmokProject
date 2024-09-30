package com.example.omokapp.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshToken {
    private Long id, expirationTime;
    private String signature;
}
