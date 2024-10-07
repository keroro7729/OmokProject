package com.example.omokapp.OmokRules;

import com.example.omokapp.Enums.GameState;

import java.util.List;

public class PracticeEngine extends Engine{
    private int curIndex;

    public PracticeEngine(OpenRule rule){
        super(rule);
        curIndex = 0;
    }

    public void moveCurIndex(int n){
        curIndex += n;
        if(curIndex < 0) curIndex = 0;
        else if(curIndex >= rule.history.size())
            curIndex = rule.history.size()-1;

        // set board & state
        rule.board = new int[rule.BOARD_SIZE][rule.BOARD_SIZE];
        for(int i=0; i<=curIndex; i++)
            rule.setCell(rule.history.get(i), i+1);
        if (rule instanceof RenjuRule)
            ((RenjuRule) rule).verifyBoard();
        rule.state = curIndex%2 == 0 ? GameState.white_playing : GameState.black_playing;
    }

    public int put(int coordinate){
        if(curIndex < rule.history.size()-1)
            rule.history = rule.history.subList(0, curIndex+1);
        curIndex++;
        return rule.put(coordinate);
    }
    public int put(int x, int y){
        if(curIndex < rule.history.size()-1)
            rule.history = rule.history.subList(0, curIndex+1);
        curIndex++;
        return rule.put(x, y);
    }

    public int getCurIndex(){ return curIndex; }
}
