package com.friends.management.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final int httpStatus;

    public ApplicationException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
