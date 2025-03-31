package com.api.spotify_gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMissingHeaderException(MissingHeaderException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleInvalidUserException(InvalidUserException ex) {
        return ex.getMessage();
    }
}
