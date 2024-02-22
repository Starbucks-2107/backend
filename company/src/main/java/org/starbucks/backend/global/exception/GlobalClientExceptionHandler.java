package org.starbucks.backend.global.exception;

import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalClientExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException() {
        ErrorResponse errorResponse = new ErrorResponse("잘못된 값을 입력 하였습니다.", "CLIENT_01");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException() {
        ErrorResponse errorResponse = new ErrorResponse("유효 하지 않은 값 입니다.", "CLIENT-02");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException() {
        ErrorResponse errorResponse = new ErrorResponse("유효 하지 않은 값 입니다.", "CLIENT-03");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
