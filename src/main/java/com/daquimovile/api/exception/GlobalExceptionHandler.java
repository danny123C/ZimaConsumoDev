package com.daquimovile.api.exception;

import com.daquimovile.api.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import javax.security.auth.login.AccountNotFoundException;
/**
 * Esta estructura es DINÁMICA porque permite enviar el código HTTP real
 * dentro del cuerpo del JSON, facilitando la lógica en Flutter.
 */
@RestControllerAdvice 
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseDto<Void>> handleBusiness(BusinessException ex) {
        // Dinámico: Status 400 (Bad Request)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.error(400, ex.getMessage()));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ResponseDto<Void>> handleHttpClientError(HttpClientErrorException ex) {
        logger.error("Error en servicio externo (Core/Octopus): {}", ex.getMessage());
        // Dinámico: Toma el status exacto que devolvió el servicio externo (401, 403, etc.)
        int statusCode = ex.getStatusCode().value();
        return new ResponseEntity<>(
            ResponseDto.error(statusCode, "Error de comunicación externa: " + ex.getStatusText()), 
            ex.getStatusCode()
        );
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ResponseDto<Void>> handleTimeout(ResourceAccessException ex) {
        logger.error("Timeout o Servidor Caído: {}", ex.getMessage());
        // Dinámico: Status 504
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                .body(ResponseDto.error(504, "El servicio externo no responde (Timeout)."));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto<Void>> handleInvalidJson(HttpMessageNotReadableException ex) {
        // Dinámico: Status 400
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.error(400, "El formato de la solicitud es incorrecto."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Void>> handleAllExceptions(Exception ex) {
        logger.error("ERROR NO CONTROLADO: ", ex);
        // Dinámico: Status 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.error(500, "Error interno en daquimovil-app."));
    }
}