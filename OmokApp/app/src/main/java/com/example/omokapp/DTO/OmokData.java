package com.example.omokapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class OmokData {
    private int gameState;
    private String history;
    public OmokData(){}
    public OmokData(String line){
        gameState = Integer.parseInt(line.substring(0, 1));
        history = line.substring(2);
    }
    @Override
    public String toString(){
        return gameState + " " + history;
    }
}
