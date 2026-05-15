package com.nicico.committee.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicatePositionException extends RuntimeException {
    public DuplicatePositionException(String message) {
        super(message);
    }
}
