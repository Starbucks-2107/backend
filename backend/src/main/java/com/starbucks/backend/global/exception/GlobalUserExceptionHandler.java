package com.starbucks.backend.global.exception;

import jakarta.persistence.EntityExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalUserExceptionHandler {
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> handleEntityExistsException () {
        ErrorResponse errorResponse = new ErrorResponse("이미 존재 하는 유저입니다.", "USER_01");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
