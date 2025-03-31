package com.api.spotify_gateway.exception;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException(String message,Throwable cause) {
        super(message,cause);
    }
}