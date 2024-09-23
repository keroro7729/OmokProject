package com.example.omokapp.Enums;

public enum GameState {
    initial(0),
    black_playing(1),
    white_playing(2),
    black_win(3),
    white_win(4),
    draw(5);

    private final int code;

    GameState(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static GameState fromCode(int code) {
        for (GameState state : GameState.values()) {
            if (state.getCode() == code) {
                return state;
            }
        }
        return null;
        //throw new IllegalArgumentException("Invalid code: " + code);
    }
}
