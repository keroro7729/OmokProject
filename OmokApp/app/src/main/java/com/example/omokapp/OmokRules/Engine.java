package com.example.omokapp.OmokRules;

import com.example.omokapp.Enums.GameState;

import java.util.List;

public class Engine {
    protected OpenRule rule;
    public Engine(OpenRule rule){
        this.rule = rule;
    }

    public int[][] getBoard(){ return rule.board; }
    public List<Integer> getHistory(){ return rule.history; }
    public GameState getState(){ return rule.state; }
    public String getHistoryString(){
        StringBuilder sb = new StringBuilder();
        for(int coor : rule.history)
            sb.append(coor).append(' ');
        return sb.toString();
    }
}
