package com.api.spotify_gateway.exception;

public class MissingHeaderException extends RuntimeException {
    public MissingHeaderException(String message) {
        super(message);
    }
}