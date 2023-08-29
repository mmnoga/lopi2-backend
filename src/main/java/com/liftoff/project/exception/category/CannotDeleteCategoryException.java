package com.liftoff.project.exception.category;

import org.springframework.http.HttpStatus;

public class CannotDeleteCategoryException extends RuntimeException{

    private final HttpStatus status;
    private final String message;

    public CannotDeleteCategoryException(String message) {
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
