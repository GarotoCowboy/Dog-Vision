package br.com.dogvision.user.infra.exception.error;

import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp,
        List<FieldError> errors
) {
    public record FieldError(
            String field,
            String message
    ) {}

    public ErrorResponse(int status, String message) {
        this(status, message, LocalDateTime.now(), null);
    }

    public ErrorResponse(int status, String message, List<FieldError> errors) {
        this(status, message, LocalDateTime.now(), errors);
    }
}
