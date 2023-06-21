package com.friends.management.exception;

import lombok.*;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;

}

