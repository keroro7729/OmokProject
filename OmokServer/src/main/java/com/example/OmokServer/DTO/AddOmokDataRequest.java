package com.example.OmokServer.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddOmokDataRequest {
    // security token? may needed
    private String dataFile;
}
