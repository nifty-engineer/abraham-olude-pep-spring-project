package com.example.exception;

public class ExistingAccountException extends RuntimeException{

    public ExistingAccountException(String message) {
        
        super(message);
    }
    
}
