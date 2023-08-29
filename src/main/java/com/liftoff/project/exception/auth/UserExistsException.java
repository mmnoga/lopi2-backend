package com.liftoff.project.exception.auth;

import org.springframework.http.HttpStatus;

public class UserExistsException extends RuntimeException{
    private final HttpStatus status;
    private final String message;

    public UserExistsException(String message) {
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
