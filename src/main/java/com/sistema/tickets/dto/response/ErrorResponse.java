package com.sistema.tickets.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<ValidationError> errors
) {
    public record ValidationError(
            String field,
            String message
    ) {}

    // Constructor de conveniencia para errores simples sin lista de campos
    public ErrorResponse(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path, null);
    }

    // Constructor de conveniencia para errores de validación de campos
    public ErrorResponse(int status, String error, String message, String path, List<ValidationError> errors) {
        this(LocalDateTime.now(), status, error, message, path, errors);
    }
}