package com.github.transitbot.api.services.exceptions;

public class StopNotFoundException extends Exception {
    public StopNotFoundException(String message){
        super(message);
    }
}
