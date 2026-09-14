package com.manuel.taskmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)

    @ResponseStatus(HttpStatus.NOT_FOUND)

    public String manejarRecursoNoEncontrados(
            RecursoNoEncontradoException ex) {

        return ex.getMessage();
    }

}
