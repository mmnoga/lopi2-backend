package com.liftoff.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class LoginException extends AuthenticationException {
    private final HttpStatus status;
    private final String message;

    public LoginException(String message) {
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