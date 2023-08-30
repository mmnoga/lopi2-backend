package com.liftoff.project.exception.storage;

import org.springframework.http.HttpStatus;

public class FileSizeExceedsLimitException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public FileSizeExceedsLimitException(String message) {
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
