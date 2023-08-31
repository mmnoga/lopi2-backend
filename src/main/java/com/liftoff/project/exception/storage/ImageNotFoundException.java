package com.liftoff.project.exception.storage;

import org.springframework.http.HttpStatus;

public class ImageNotFoundException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public ImageNotFoundException(String message) {
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