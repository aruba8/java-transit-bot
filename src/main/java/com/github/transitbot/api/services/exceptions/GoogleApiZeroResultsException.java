package com.github.transitbot.api.services.exceptions;

/**
 * Google Api Zero Results Exception.
 */
public class GoogleApiZeroResultsException extends Exception {

    /**
     * constructor.
     *
     * @param message message
     */
    public GoogleApiZeroResultsException(String message) {
        super(message);
    }
}
