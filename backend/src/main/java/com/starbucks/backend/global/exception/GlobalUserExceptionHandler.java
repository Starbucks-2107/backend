package com.starbucks.backend.global.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalUserExceptionHandler {
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> handleEntityExistsException() {
        ErrorResponse errorResponse = new ErrorResponse("이미 존재 하는 유저입니다.", "USER_01");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException() {
        ErrorResponse errorResponse = new ErrorResponse("해당 유저를 찾을 수 없습니다.", "USER_02");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException() {
        ErrorResponse errorResponse = new ErrorResponse("해당 유저를 찾을 수 없습니다.", "USER_02");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException() {
        ErrorResponse errorResponse = new ErrorResponse("인증 요청 유저와 로그인 유저가 같지 않습니다.", "USER_03");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
