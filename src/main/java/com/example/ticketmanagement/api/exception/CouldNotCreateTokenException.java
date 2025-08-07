package com.example.ticketmanagement.api.exception;

public class CouldNotCreateTokenException extends RuntimeException {

    public CouldNotCreateTokenException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
