package com.github.transitbot.api.services.exceptions;

/**
 * Stop not found exception.
 */
public class StopNotFoundException extends Exception {

    /**
     * constructor.
     *
     * @param message message
     */
    public StopNotFoundException(String message) {
        super(message);
    }
}
