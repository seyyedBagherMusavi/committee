package com.nicico.committee.util.base.enumeration;

import com.nicico.copper.base.IErrorCode;
import org.springframework.http.HttpStatus;

public enum ErrorType implements IErrorCode {
    SEARCH_METHOD_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "err.search-method-not-found"),
    INVALID_JSON_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, "err.invalid-json-format"),
    ILLEGAL_ACCESS(HttpStatus.INTERNAL_SERVER_ERROR, "err.illegal-access"),
    UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "err.unknown-exception"),
    ;

    @Override
    public String getName() {
        return this.message;
    }

    @Override
    public Integer getHttpStatusCode() {
        return this.status.value();
    }

    private final HttpStatus status;
    private final String message;

    ErrorType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
