package com.manuel.taskmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)

    @ResponseStatus(HttpStatus.NOT_FOUND)

    public ErrorResponse manejarRecursoNoEncontrado(
            RecursoNoEncontradoException ex) {

        return new ErrorResponse(
                ex.getMessage());
    }
}