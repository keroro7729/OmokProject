package com.example.omokapp.Enums;

public enum CellState {

    three_prohibition(-1),
    four_prohibition(-2),
    six_prohibition(-3),
    omok(-100);

    private final int code;

    CellState(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static CellState fromCode(int code) {
        for (CellState state : CellState.values()) {
            if (state.getCode() == code) {
                return state;
            }
        }
        return null;
        //throw new IllegalArgumentException("Invalid code: " + code);
    }
}
