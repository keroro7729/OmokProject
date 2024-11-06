package com.example.OmokServer.Enums;

public enum PutError {
    not_playing(-11),
    index_out(-12),
    not_empty(-13),
    prohibition(-14);

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

