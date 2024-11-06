package com.example.omokapp.OmokRules;

import android.util.Log;

import com.example.omokapp.Enums.CellState;
import com.example.omokapp.Enums.GameState;
import com.example.omokapp.Enums.PutError;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class RenjuRule extends OpenRule {

    public RenjuRule(){
        super();
    }
    public RenjuRule(RenjuRule copy){
        for(int i=0; i<BOARD_SIZE; i++)
            for(int j=0; j<BOARD_SIZE; j++)
                this.board[i][j] = copy.board[i][j];
        this.history = new ArrayList<>(copy.history);
        this.state = copy.state;
        verifyBoard();
    }

    protected static final int[][] FOUR_EXCEPTIONS = {
            {-1, 1, 1, 1, 0, 0, 0, 1, 1, 1, -1},
            {-1, 1, 0, 1, 0, 1, 0, 1, -1}
    };
    protected static final int[] FIVE = {-1, 1, 1, 1, 1, 1, -1};
    protected static final int[] OPEN_FOUR = {-1, 0, 1, 1, 1, 1, 0, -1};

    @Override
    public int put(int x, int y){
        if(state != GameState.black_playing &&
           state != GameState.white_playing)
            return PutError.not_playing.getCode();
        else if(indexOut(x, y))
            return PutError.index_out.getCode();
        else if(board[x][y] > 0)
            return PutError.not_empty.getCode();
        else if(board[x][y] < 0 && state == GameState.black_playing)
            return PutError.prohibition.getCode();

        history.add(getCoordinate(x, y));
        board[x][y] = history.size();

        if(isOmok(x, y)){
            if(state == GameState.black_playing){
                state = GameState.black_win;
                return state.getCode();
            }
            else if(state == GameState.white_playing){
                state = GameState.white_win;
                return state.getCode();
            }
        }
        else if(history.size() >= DRAW_THRESHOLD){
            state = GameState.draw;
            return state.getCode();
        }
        verifyBoard();

        if(state == GameState.black_playing)
            state = GameState.white_playing;
        else if(state == GameState.white_playing)
            state = GameState.black_playing;
        return state.getCode();
    }

    @Override
    protected boolean isOmok(int x, int y){
        Log.d("RenjuRule", "countConsecutive result: "+countConsecutive(x, y));
        if(state == GameState.black_playing)
            return countConsecutive(x, y, 1) == 5;
        else if(state == GameState.white_playing)
            return countConsecutive(x, y, 2) >= 5;
        Log.e("RenjuRule", "isOmok() reached unexpected code");
        return false;
    }

    protected void verifyBoard(){
        // init & check six prohibition
        Set<Integer> list = new HashSet<>();
        for(int coor=0; coor<BOARD_SIZE*BOARD_SIZE; coor++){
            if(getCell(coor) > 0) continue;
            int count = countConsecutive(getX(coor), getY(coor), 1);
            if(count > 5) setCell(coor, CellState.six_prohibition.getCode());
            else if(count != 5) {
                setCell(coor, 0);
                list.add(coor);
            }
        }
        // check four prohibition
        Set<Integer> tmp = new HashSet<>();
        for(int coor : list)
            if(isFourProhibition(coor))
                tmp.add(coor);
        for(int coor : tmp)
            setCell(coor, CellState.four_prohibition.getCode());
        list.removeAll(tmp);
        tmp.clear();
        // check three prohibition
        for(int coor : list)
            if(isThreeProhibition(coor))
                tmp.add(coor);
        for(int coor : tmp)
            setCell(coor, CellState.three_prohibition.getCode());
    }

    protected boolean isFourProhibition(int coor){
        return isFourProhibition(getX(coor), getY(coor));
    }
    protected boolean isFourProhibition(int x, int y){
        for(int d=0; d<4; d++){
            for(int[] pattern : FOUR_EXCEPTIONS){
                int dx = x - DIRX[d] * (pattern.length/2);
                int dy = y - DIRY[d] * (pattern.length/2);
                if(indexOut(dx, dy)) continue;
                if(perfectMatch(dx, dy, d, pattern)) return true;
            }
        }

        int count = 0;
        for(int d=0; d<4; d++){
            for(int l=1-FIVE.length; l<=0; l++){
                int dx = x + DIRX[d] * l;
                int dy = y + DIRY[d] * l;
                //if(indexOut(dx, dy)) continue;//
                if(almostMatch(dx, dy, d, FIVE) != -1){
                    count++;
                    break;
                }
            }
        }
        return count >= 2;
    }

    protected boolean isThreeProhibition(int coor){
        return isThreeProhibition(getX(coor), getY(coor));
    }
    protected boolean isThreeProhibition(int x, int y){
        board[x][y] = 1;
        int count = 0;
        for(int d=0; d<4; d++){
            for(int l=1-OPEN_FOUR.length; l<=0; l++){
                int dx = x + DIRX[d] * l;
                int dy = y + DIRY[d] * l;
                int coordinate = almostMatch(dx, dy, d, OPEN_FOUR);
                if(coordinate == -1) continue;

                // check false prohibition
                int ddx = getX(coordinate), ddy = getY(coordinate);
                if(!isFourProhibition(ddx, ddy)){
                    count++;
                    break;
                }
                boolean right = isBlack(ddx+DIRX[d], ddy+DIRY[d]);
                boolean left = isBlack(ddx-DIRX[d], ddy-DIRY[d]);
                if(!left && right){
                    ddx += DIRX[d] * 5;
                    ddy += DIRY[d] * 5;
                    if(indexOut(ddx, ddy) || board[ddx][ddy] != 0) continue;
                    if(isFourProhibition(ddx-DIRX[d], ddy-DIRY[d])) continue;
                    count++;
                    break;
                }
                else if(left && !right){
                    ddx -= DIRX[d] * 5;
                    ddy -= DIRY[d] * 5;
                    if(indexOut(ddx, ddy) || board[ddx][ddy] != 0) continue;
                    if(isFourProhibition(ddx+DIRX[d], ddy+DIRY[d])) continue;
                    count++;
                    break;
                }
            }
        }
        board[x][y] = 0;
        return count >= 2;
    }
    protected boolean putAndCheckFourProhibition(int x, int y){
        board[x][y] = 1;
        boolean result = isFourProhibition(x, y);
        board[x][y] = 0;
        return result;
    }

    private int almostMatch(int x, int y, int d, int[] pattern){
        return almostMatch(x, y, d, pattern, 1);
    }
    protected int almostMatch(int x, int y, int d, int[] pattern, int color){
        int coor = -1;
        for(int i=0; i<pattern.length; i++){
            int dx = x + DIRX[d] * i, dy = y + DIRY[d] * i;
            if(pattern[i] == -1){
                if(color == 1 && !indexOut(dx, dy) && isBlack(dx, dy))
                    return -1;
            }
            else if(pattern[i] == 0){
                if(indexOut(dx, dy)) return -1;
                else if(!putable(dx, dy, color)) return -1;
            }
            else if(pattern[i] == 1){
                if(indexOut(dx, dy)) return -1;
                if(color != getColor(dx, dy)){
                    if(!putable(dx, dy, color)) return -1;
                    else if(coor == -1) coor = getCoordinate(dx, dy);
                    else return -1;
                }
            }
        }
        return coor;
    }
    private boolean perfectMatch(int x, int y, int d, int[] pattern){
        return perfectMatch(x, y, d, pattern, 1);
    }
    protected boolean perfectMatch(int x, int y, int d, int[] pattern, int color){
        for(int i=0; i<pattern.length; i++){
            int dx = x + DIRX[d] * i, dy = y + DIRY[d] * i;
            if(pattern[i] == -1){
                if(color == 1 && !indexOut(dx, dy) && isBlack(dx, dy))
                    return false;
            }
            else if(pattern[i] == 0){
                if(indexOut(dx, dy)) return false;
                else if(putable(dx, dy, color)) return false;
            }
            else if(pattern[i] == 1){
                if(indexOut(dx, dy)) return false;
                else if(color != getColor(dx, dy)) return false;
            }
        }
        return true;
    }

    protected boolean isBlack(int x, int y){
        return board[x][y]%2 == 1;
    }
    protected boolean putable(int x, int y, int color){
        if(color == 1)
            return board[x][y] == 0;
        else if(color == 2)
            return board[x][y] <= 0;
        else throw new RuntimeException("RenjuRule putable Error illegal argument color: "+color);
    }
}
