package com.liftoff.project.exception.order;

import org.springframework.http.HttpStatus;

public class OrderExistsException extends RuntimeException{
    private final HttpStatus status;
    private final String message;

    public OrderExistsException(String message) {
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
