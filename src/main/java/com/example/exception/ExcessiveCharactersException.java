package com.example.exception;

public class ExcessiveCharactersException extends RuntimeException{

    public ExcessiveCharactersException(String message) {
        super(message);
    }    
}
