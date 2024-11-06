package com.example.OmokServer.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter @AllArgsConstructor
public class AIRequest {
    private List<Integer> gameLog;
    public AIRequest(){}
}
