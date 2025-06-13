package com.aaalace.orderservice.presentation.handler;

import com.aaalace.orderservice.domain.exception.BadRequestError;
import com.aaalace.orderservice.domain.exception.InternalServerError;
import com.aaalace.orderservice.domain.generic.GenericJsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestError.class)
    public ResponseEntity<GenericJsonResponse<Object>> handleRuntimeException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(GenericJsonResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<GenericJsonResponse<Object>> handleInternalException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(GenericJsonResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericJsonResponse<Object>> handleGenericException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(GenericJsonResponse.failure("Internal server error"));
    }
}