package com.example.exception;

public class CustomDataAccessException extends Exception{

    public CustomDataAccessException(String message, Exception e) {
        super(message);
    }    
}
