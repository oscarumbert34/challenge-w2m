package com.w2m.superheroe.controllers;

import com.w2m.superheroe.exceptions.SuperHeroeException;
import com.w2m.superheroe.models.entities.dtos.ErrorApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ValidationException;

@ControllerAdvice
@Slf4j
public class Handler {


    @ExceptionHandler({ValidationException.class,  InvalidDataAccessApiUsageException.class})
    public ResponseEntity<ErrorApi> handlerException(ValidationException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getError(HttpStatus.BAD_REQUEST, e.getMessage()));

    }

    @ExceptionHandler({SuperHeroeException.class})
    public ResponseEntity<ErrorApi> handlerException(RuntimeException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getError(HttpStatus.NOT_FOUND, e.getMessage()));

    }
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorApi> handlerException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getError(HttpStatus.BAD_REQUEST, e.getMessage()));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApi> handlerException(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));

    }

    private ErrorApi getError(HttpStatus status, String message){
        return ErrorApi.builder().code(status.value()).message(message).build();
    }
}
