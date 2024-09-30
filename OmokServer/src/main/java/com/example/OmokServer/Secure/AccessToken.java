package com.example.OmokServer.Secure;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccessToken {
    private Long id, expirationTime;
    private String signature;
}
