package com.liftoff.project.exception.cookie;

import org.springframework.http.HttpStatus;

public class CookieNotFoundException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public CookieNotFoundException(String message) {
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