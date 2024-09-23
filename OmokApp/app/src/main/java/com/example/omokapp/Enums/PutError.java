package com.example.omokapp.Enums;

public enum PutError {
    not_playing(-1),
    index_out(-2),
    not_empty(-3),
    prohibition(-4);

    private final int code;

    PutError(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PutError fromCode(int code) {
        for (PutError error : PutError.values()) {
            if (error.getCode() == code) {
                return error;
            }
        }
        return null;
        //throw new IllegalArgumentException("Invalid code: " + code);
    }
}
