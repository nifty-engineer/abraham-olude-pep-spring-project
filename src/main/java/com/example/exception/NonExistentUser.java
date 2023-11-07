package com.example.exception;

public class NonExistentUser extends RuntimeException{

    public NonExistentUser(String message) {
        super(message);
    }    
}
