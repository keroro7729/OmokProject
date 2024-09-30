package com.example.OmokServer.Exceptions;

public class LoginFailException extends RuntimeException {
    public LoginFailException(String message){
        super(message);
    }
}
