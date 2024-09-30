package com.example.OmokServer.Secure;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RefreshToken {
    private Long id, expirationTime;
    private String signature;
}
