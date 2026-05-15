package com.nicico.companies.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * custom exception for application
 * @author Seyyed
 */
@Getter
@Setter
public class ApplicationException extends RuntimeException {

    private HttpStatus status;
    private String message;
    private Object[] args;

    public ApplicationException(HttpStatus status, String message,Object[] args) {
        super(message);
        this.status = status;
        this.message = message;
        this.args = args;
    }
}