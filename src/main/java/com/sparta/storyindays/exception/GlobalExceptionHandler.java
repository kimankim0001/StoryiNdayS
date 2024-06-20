package com.sparta.storyindays.exception;

import com.sparta.storyindays.dto.ExceptionResDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResDto> IllgalArgumentExceptionHandler(IllegalArgumentException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResDto(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResDto> AuthenticationExceptionHandler(AuthenticationException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

}
