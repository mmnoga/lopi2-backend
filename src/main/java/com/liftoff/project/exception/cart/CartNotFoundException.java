package com.liftoff.project.exception.cart;

import org.springframework.http.HttpStatus;

public class CartNotFoundException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public CartNotFoundException(String message) {
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