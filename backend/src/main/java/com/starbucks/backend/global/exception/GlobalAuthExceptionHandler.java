package com.starbucks.backend.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalAuthExceptionHandler {
    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<?> handleAuthenticationServiceException() {
        ErrorResponse errorResponse = new ErrorResponse("사용자 인증에 실패 하였습니다.", "AUTH_01");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException() {
        ErrorResponse errorResponse = new ErrorResponse("해당 유저를 찾을 수 없습니다.", "AUTH_02");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException() {
        ErrorResponse errorResponse = new ErrorResponse("잘못된 비밀먼호를 입력하였습니다.", "AUTH_03");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
