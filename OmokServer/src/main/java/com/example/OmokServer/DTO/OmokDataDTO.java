package com.example.OmokServer.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OmokDataDTO {
    private int gameState;
    private String history;
    public OmokDataDTO(){}
    public OmokDataDTO(String line){
        gameState = Integer.parseInt(line.substring(0, 1));
        history = line.substring(2);
    }
    @Override
    public String toString(){
        return gameState + " " + history;
    }
}
