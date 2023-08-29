package com.liftoff.project.exception.auth;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException{

    private final HttpStatus status;
    private final String message;

    public UserNotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
