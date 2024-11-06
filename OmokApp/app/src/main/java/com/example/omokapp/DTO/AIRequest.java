package com.example.omokapp.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AIRequest {
    private List<Integer> gameLog;
    public AIRequest(){}
}
