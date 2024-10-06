package com.example.omokapp.OmokRules;

import com.example.omokapp.Enums.GameState;

public class RenjuEngine extends RenjuRule{
    private int curIndex;

    public RenjuEngine(){
        super();
        curIndex = 0;
    }

    public void moveCurIndex(int n){
        curIndex += n;
        if(curIndex < 0) curIndex = 0;
        else if(curIndex >= history.size())
            curIndex = history.size()-1;

        // set board & state
        board = new int[BOARD_SIZE][BOARD_SIZE];
        for(int i=0; i<=curIndex; i++)
            setCell(history.get(i), i+1);
        verifyBoard();
        state = curIndex%2 == 0 ? GameState.white_playing : GameState.black_playing;
    }

    @Override
    public int put(int x, int y){
        if(curIndex < history.size()-1)
            history = history.subList(0, curIndex+1);
        curIndex++;
        return super.put(x, y);
    }

    public int indexMargin(){
        return history.size() - curIndex - 1;
    }
    public int getCurIndex(){ return curIndex; }
}
