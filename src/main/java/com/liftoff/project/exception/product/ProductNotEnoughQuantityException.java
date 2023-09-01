package com.liftoff.project.exception.product;

import org.springframework.http.HttpStatus;

public class ProductNotEnoughQuantityException extends RuntimeException{

    private final HttpStatus status;
    private final String message;

    public ProductNotEnoughQuantityException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
