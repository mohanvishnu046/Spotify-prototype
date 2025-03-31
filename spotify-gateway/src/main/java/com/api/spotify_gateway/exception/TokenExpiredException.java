package com.api.spotify_gateway.exception;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
