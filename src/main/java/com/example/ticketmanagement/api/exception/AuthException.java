package com.example.ticketmanagement.api.exception;

public class AuthException extends RuntimeException {

    public AuthException(final String message) {
        super(message);
    }
}
