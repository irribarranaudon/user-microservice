package com.irribarra.microservice.app.usermicroservice.util;

import com.irribarra.microservice.app.usermicroservice.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * Clase util para el manejo de errores en el microservicio.
 */
public class ExceptionUtil {

    public ExceptionUtil() {
    }

    public static void throwExecIf(boolean invalid, String message) throws BusinessException {
        if (invalid) {
            throw new BusinessException(message, HttpStatus.BAD_REQUEST);
        }
    }

    public static void throwExecIf(boolean invalid, String message, HttpStatus httpStatus) throws BusinessException {
        if (invalid) {
            throw new BusinessException(message, httpStatus);
        }
    }
}
