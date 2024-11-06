package com.example.OmokServer.Engine;

import com.example.OmokServer.Enums.GameState;

import java.util.ArrayList;
import java.util.List;

public class OpenRule {
    protected int[][] board;
    protected List<Integer> history;
    protected GameState state;

    protected final int DRAW_THRESHOLD = 200;
    protected final int[] DIRX = {-1, -1, 0, 1, 1, 1, 0, -1};
    protected final int[] DIRY = {0, 1, 1, 1, 0, -1, -1, -1};
    protected final int BOARD_SIZE = 15;

    public OpenRule(){
        init();
    }
    protected void init(){
        board = new int[BOARD_SIZE][BOARD_SIZE];
        history = new ArrayList<>();
        state = GameState.black_playing;
        put(7, 7);
    }

    public int put(int coordinate){
        return put(getX(coordinate), getY(coordinate));
    }
    public int put(int x, int y){
        if(state != GameState.black_playing &&
                state != GameState.white_playing) return -1;
        if(indexOut(x, y)) return -2;
        if(board[x][y] != 0) return -3;

        history.add(getCoordinate(x, y));
        board[x][y] = history.size();

        if(history.size() >= DRAW_THRESHOLD){
            state = GameState.draw;
            return state.getCode();
        }
        else if(isOmok(x, y)){
            if(state == GameState.black_playing){
                state = GameState.black_win;
                return state.getCode();
            }
            else if(state == GameState.white_playing){
                state = GameState.white_win;
                return state.getCode();
            }
        }

        if(state == GameState.black_playing)
            state = GameState.white_playing;
        else if(state == GameState.white_playing)
            state = GameState.black_playing;
        return state.getCode();
    }


    protected boolean isOmok(int x, int y){
        return countConsecutive(x, y) >= 5;
    }

    protected int countConsecutive(int x, int y){
        int color = getColor(x, y);
        return countConsecutive(x, y, color);
    }
    protected int countConsecutive(int x, int y, int color){
        int dx, dy, count, result = 0;
        for(int d=0; d<4; d++){
            count = 0;
            for(int l=1; l<=4; l++) {
                dx = x + DIRX[d] * l;
                dy = y + DIRY[d] * l;
                if (indexOut(dx, dy) || getColor(dx, dy) != color) break;
                else count++;
            }
            for(int l=1; l<=4; l++){
                dx = x - DIRX[d]*l;
                dy = y - DIRY[d]*l;
                if(indexOut(dx, dy) || getColor(dx, dy) != color) break;
                else count++;
            }
            count++;
            if(count == 5) return 5; // return 5 first
            result = Math.max(result, count);
        }
        return result;
    }

    protected boolean indexOut(int x, int y){
        return x < 0 || 14 < x || y < 0 || 14 < y;
    }

    protected int getCoordinate(int x, int y){
        return x * BOARD_SIZE + y;
    }

    protected int getX(int coordinate){
        return coordinate / BOARD_SIZE;
    }

    protected int getY(int coordinate){
        return coordinate % BOARD_SIZE;
    }

    protected int getColor(int x, int y){
        if(board[x][y] == 0) return -1;
        else if(board[x][y]%2 == 1) return 1;
        else return 2;
    }

    public int[][] getBoard(){ return board; }
    public List<Integer> getHistory(){ return history; }
    public GameState getState(){ return state; }
    protected int getCell(int coordinate){ return board[getX(coordinate)][getY(coordinate)]; }
    protected void setCell(int coordinate, int state){ board[getX(coordinate)][getY(coordinate)] = state; }
}
