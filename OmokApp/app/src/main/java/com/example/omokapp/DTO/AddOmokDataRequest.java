package com.example.omokapp.DTO;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class AddOmokDataRequest {
    // security token? may needed
    private String dataFile;
}
