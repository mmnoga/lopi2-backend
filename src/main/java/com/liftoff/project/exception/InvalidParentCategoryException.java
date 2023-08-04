package com.liftoff.project.exception;

import org.springframework.http.HttpStatus;

public class InvalidParentCategoryException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public InvalidParentCategoryException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
