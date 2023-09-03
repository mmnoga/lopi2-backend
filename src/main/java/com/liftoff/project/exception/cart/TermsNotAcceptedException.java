package com.liftoff.project.exception.cart;

import org.springframework.http.HttpStatus;

public class TermsNotAcceptedException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public TermsNotAcceptedException(String message) {
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
