package com.liftoff.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class) // Security
    public ResponseEntity<Map<String, String>> handleUserSecurityDetailsNotFoundException(UsernameNotFoundException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleInvalidLoginFieldsException(MethodArgumentNotValidException ex) {

        return createErrorResponse(ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, String>> handleBusinessException(BusinessException ex) {
        return createErrorResponse(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<Map<String, String>> handleTechnicalException(TechnicalException ex) {
        return createErrorResponse(ex.getMessage(), ex.getStatus());
    }


    private ResponseEntity<Map<String, String>> createErrorResponse(String errorMessage, HttpStatus status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", errorMessage);
        return ResponseEntity.status(status).body(errorResponse);
    }

}
