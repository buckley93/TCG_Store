package com.yourco.tcg.common;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * TODO (class):
 * - Map exceptions to ApiError with correct HTTP status (404, 409, 400, 500).
 *
 * Annotations / types: @RestControllerAdvice
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiError.of(404, "NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fields = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> fields.put(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest()
                .body(ApiError.of(400, "VALIDATION_FAILED", "Invalid request", fields));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex) {
        // TODO: log the exception here once logging is set up
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(500, "INTERNAL_ERROR", "Something went wrong"));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiError.of(409, "CONFLICT", ex.getMessage()));
    }

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ApiError> handleOutOfStock(OutOfStockException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiError.of(409, "OUT_OF_STOCK", ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiError.of(409, "DATA_INTEGRITY_VIOLATION", "That record conflicts with existing data"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleBadParam(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest()
                .body(ApiError.of(400, "BAD_PARAMETER", "Invalid value for '" + ex.getName() + "'"));
    }

}
