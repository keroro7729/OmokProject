package com.example.OmokServer.Engine;

import com.example.OmokServer.Enums.GameState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RenjuEngine extends RenjuRule{

    public RenjuEngine(){
        super();
    }
    public RenjuEngine(RenjuEngine copy){
        board = new int[15][15];
        for(int i=0; i<15; i++)
            board[i] = Arrays.copyOf(copy.board[i], 15);
        history = new ArrayList<>(copy.history);
        state = copy.state;
    }
    public RenjuEngine(List<Integer> history){
        board = new int[15][15];
        this.history = new ArrayList<>();
        for(int i=0; i<history.size()-1; i++){
            int coor = history.get(i);
            board[getX(coor)][getY(coor)] = i+1;
            this.history.add(coor);
        }
        state = history.size()%2 == 1 ? GameState.black_playing : GameState.white_playing;
        put(history.get(history.size()-1));
    }
    public void reset(){
        board = new int[15][15];
        history = new ArrayList<>();
        state = GameState.black_playing;
        put(7, 7);
    }

    public boolean isEnd(){
        return state == GameState.draw ||
                state == GameState.black_win ||
                state == GameState.white_win;
    }

    public int getWinner(){
        return state.getCode();
    }

    public List<Integer> getValidActions(){
        List<Integer> result = new ArrayList<>();
        int color = state == GameState.black_playing ? 1 : 2;
        for(int coor=0; coor<225; coor++){
            if(putable(coor, color))
                result.add(coor);
        }
        return result;
    }

    public void makeMove(int coordinate){
        put(coordinate);
    }
}
