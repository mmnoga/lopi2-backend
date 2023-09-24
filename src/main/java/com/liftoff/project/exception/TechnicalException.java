package com.liftoff.project.exception;

import org.springframework.http.HttpStatus;

public class TechnicalException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public TechnicalException(String message) {
        super(message);
        this.status = HttpStatus.FOUND;
        this.message = message;
    }

    public TechnicalException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}

