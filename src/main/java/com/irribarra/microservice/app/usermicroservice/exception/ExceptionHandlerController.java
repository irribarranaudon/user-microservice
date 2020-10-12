package com.irribarra.microservice.app.usermicroservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para manejor en retorno estandar de mensajeria para casos de error,
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> customBusiness(BusinessException ex) {
        Map<String, Object> response = new HashMap<>();
        log.error("{}", ex.getHttpStatus());
        log.error(ex.getMessage());
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, ex.getHttpStatus());

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> customRuntime(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        log.error("error :", ex);
        response.put("message", "Error interno en microservicio users.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}