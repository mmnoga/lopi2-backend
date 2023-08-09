package com.liftoff.project.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistedException extends RuntimeException{
    private final HttpStatus status;
    private final String message;

    public UserAlreadyExistedException(String message) {
        super(message);
        this.status = HttpStatus.FOUND;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
