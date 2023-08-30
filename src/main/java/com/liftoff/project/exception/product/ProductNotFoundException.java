package com.liftoff.project.exception.product;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public ProductNotFoundException(String message) {
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