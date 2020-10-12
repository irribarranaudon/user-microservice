package com.irribarra.microservice.app.usermicroservice.exception;

import org.springframework.http.HttpStatus;

public class BussinesException extends Exception {

    private final HttpStatus httpStatus;

    public BussinesException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public BussinesException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
