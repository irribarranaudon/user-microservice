package com.irribarra.microservice.app.usermicroservice.util;

import com.irribarra.microservice.app.usermicroservice.exception.BussinesException;
import org.springframework.http.HttpStatus;

public class ExceptionUtil {

    public ExceptionUtil() {
    }

    public static void throwExcecIfFalse(boolean valid, String message) throws BussinesException {
        throwExecIf(!valid, message);
    }

    public static void throwExcecIfFalse(boolean valid, String message, HttpStatus httpStatus) throws BussinesException {
        throwExecIf(!valid, message, httpStatus);
    }

    public static void throwExecIf(boolean invalid, String message) throws BussinesException {
        if (invalid) {
            throw new BussinesException(message, HttpStatus.BAD_REQUEST);
        }
    }

    public static void throwExecIf(boolean invalid, String message, HttpStatus httpStatus) throws BussinesException {
        if (invalid) {
            throw new BussinesException(message, httpStatus);
        }
    }
}
