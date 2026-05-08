package com.daquimovile.api.exception;

import com.daquimovile.api.dto.ResponseDto;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import javax.security.auth.login.AccountNotFoundException;

@RestControllerAdvice // Cambiado de @ControllerAdvice para mejor soporte de JSON
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1. ELIMINAMOS LA DUPLICIDAD: Solo un @ExceptionHandler(Exception.class)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleAllExceptions(Exception ex) {
        logger.error("Error inesperado", ex);
        ResponseDto response = new ResponseDto(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Ocurrió un error interno inesperado: " + ex.getMessage(),
            JsonNodeFactory.instance.objectNode()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 2. Errores de comunicación con el Core (HttpClientErrorException)
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ResponseDto> handleHttpClientError(HttpClientErrorException ex) {
        logger.error("Error en servicio externo", ex);
        ResponseDto response = new ResponseDto(
            ex.getStatusCode().value(),
            "Error en la comunicación con el servicio externo: " + ex.getStatusText(),
            null
        );
        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    // 3. Error cuando la cuenta no existe
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ResponseDto> handleAccountNotFound(AccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto(404, ex.getMessage(), null));
    }

    // 4. Tus excepciones de negocio (BusinessException)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseDto> handleBusiness(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto(400, ex.getMessage(), null));
    }
}