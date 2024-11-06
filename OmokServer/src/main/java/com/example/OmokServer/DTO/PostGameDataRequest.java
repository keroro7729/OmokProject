package com.example.OmokServer.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostGameDataRequest {
    private String gameLog;
    private int resultCode;
}
