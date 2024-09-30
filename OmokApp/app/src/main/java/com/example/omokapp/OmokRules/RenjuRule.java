package com.example.omokapp.OmokRules;

import com.example.omokapp.Enums.GameState;

public class RenjuRule extends OpenRule {

    private static final int[][] FOUR_EXCEPTIONS = {
            {-1, 1, 1, 1, 0, 0, 0, 1, 1, 1, -1},
            {-1, 1, 0, 1, 0, 1, 0, 1, -1}
    };
    private static final int[] FIVE = {-1, 1, 1, 1, 1, 1, -1};
    private static final int[] OPEN_FOUR = {-1, 0, 1, 1, 1, 1, 0, -1};

    @Override
    public int put(int x, int y){
        if(state != GameState.black_playing &&
           state != GameState.white_playing) return -1;
        if(indexOut(x, y)) return -2;
        if(board[x][y] != 0) return -3;

        history.add(getCoordinate(x, y));
        board[x][y] = history.size();

        int count = countConsecutive(x, y);
        if(count >= 5){
            if(state == GameState.black_playing && count == 5){
                state = GameState.black_win;
                return state.getCode();
            }
            else if(state == GameState.black_playing && count > 5){
                // six prohibition
                history.remove(history.size()-1);
                board[x][y] = 0;
                return -4;
            }
            else if(state == GameState.white_playing){
                state = GameState.white_win;
                return state.getCode();
            }
        }
        else if(isProhibition(x, y)){
            history.remove(history.size()-1);
            board[x][y] = 0;
            return -4;
        }
        else if(history.size() >= DRAW_THRESHOLD){
            state = GameState.draw;
            return state.getCode();
        }

        if(state == GameState.black_playing)
            state = GameState.white_playing;
        else if(state == GameState.white_playing)
            state = GameState.black_playing;
        return state.getCode();
    }

    @Override
    protected boolean isOmok(int x, int y){
        if(state == GameState.black_playing)
            return countConsecutive(x, y) == 5;
        else return countConsecutive(x, y) >= 5;
    }

    protected boolean isProhibition(int x, int y){
        if(state != GameState.black_playing) return false;
        if(isFourProhibition(x, y)) return true;
        if(isThreeProhibition(x, y)) return true;
        return false;
    }

    protected boolean isFourProhibition(int x, int y){
        for(int d=0; d<4; d++){
            for(int[] pattern : FOUR_EXCEPTIONS){
                int dx = x - DIRX[d] * (pattern.length/2);
                int dy = y - DIRY[d] * (pattern.length/2);
                if(perfectMatch(dx, dy, d, pattern))
                    return true;
            }
        }

        int count = 0;
        for(int d=0; d<4; d++){
            for(int l=1-FIVE.length; l<=0; l++){
                int dx = x + DIRX[d] * l;
                int dy = y + DIRY[d] * l;
                if(almostMatch(dx, dy, d, FIVE) != -1){
                    count++;
                    break;
                }
            }
        }
        return count >= 2;
    }

    protected boolean putAndCheckFourProhibition(int x, int y){
        board[x][y] = 1;
        boolean result = isFourProhibition(x, y);
        board[x][y] = 0;
        return result;
    }

    protected boolean isThreeProhibition(int x, int y){
        int count = 0;
        for(int d=0; d<4; d++){
            for(int l=1-OPEN_FOUR.length; l<=0; l++){
                int dx = x + DIRX[d] * l;
                int dy = y + DIRY[d] * l;
                int coordinate = almostMatch(dx, dy, d, OPEN_FOUR);
                if(coordinate == -1) continue;

                // check false prohibition
                int ddx = getX(coordinate), ddy = getY(coordinate);
                if(!putAndCheckFourProhibition(ddx, ddy)){
                    count++;
                    break;
                }
                boolean right = isBlack(ddx+DIRX[d], ddy+DIRY[d]);
                boolean left = isBlack(ddx-DIRX[d], ddy-DIRY[d]);
                if(!left && right){
                    ddx += DIRX[d] * 5;
                    ddy += DIRY[d] * 5;
                    if(indexOut(ddx, ddy) || board[ddx][ddy] != 0) continue;
                    if(putAndCheckFourProhibition(ddx, ddy)) continue;
                    count++;
                    break;
                }
                else if(left && !right){
                    ddx -= DIRX[d] * 5;
                    ddy -= DIRY[d] * 5;
                    if(indexOut(ddx, ddy) || board[ddx][ddy] != 0) continue;
                    if(putAndCheckFourProhibition(ddx, ddy)) continue;
                    count++;
                    break;
                }
            }
        }
        return count >= 2;
    }

    protected int almostMatch(int x, int y, int d, int[] pattern){
        int coor = -1;
        for(int i=0; i<pattern.length; i++){
            int dx = x + DIRX[d] * i, dy = y + DIRY[d] * i;
            if(pattern[i] == -1){
                if(indexOut(dx, dy)) continue;
                if(isBlack(dx, dy)) return -1;
            }
            else if(pattern[i] == 0){
                if(indexOut(dx, dy)) return -1;
                if(board[dx][dy] > 0) return -1;
            }
            else if(pattern[i] == 1){
                if(indexOut(dx, dy)) return -1;
                if(isBlack(dx, dy)){
                    if(board[x][y] > 0) return -1;
                    if(coor == -1) coor = getCoordinate(dx, dy);
                    else return -1;
                }
            }
        }
        return coor;
    }

    protected boolean perfectMatch(int x, int y, int d, int[] pattern){
        for(int i=0; i<pattern.length; i++){
            int dx = x + DIRX[d] * i, dy = y + DIRY[d] * i;
            if(pattern[i] == -1){
                if(indexOut(x, y)) continue;
                if(isBlack(dx, dy)) return false;
            }
            else if(pattern[i] == 0){
                if(indexOut(dx, dy)) return false;
                if(board[dx][dy] > 0) return false;
            }
            else if(pattern[i] == 1){
                if(indexOut(dx, dy)) return false;
                if(isBlack(dx, dy)) return false;
            }
        }
        return true;
    }

    protected boolean isBlack(int x, int y){
        return board[x][y]%2 == 1;
    }
}
