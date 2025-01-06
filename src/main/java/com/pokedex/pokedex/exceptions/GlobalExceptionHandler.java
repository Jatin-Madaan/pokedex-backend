package com.pokedex.pokedex.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InfoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleInfoNotFound(InfoNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), "Resource not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    // Handle ResourceNotFoundException
    @ExceptionHandler(EvolutionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEvolutionNotFound(EvolutionNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), "Resource not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), ex.getLocalizedMessage(), "An error occurred");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
