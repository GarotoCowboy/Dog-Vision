package br.com.dogvision.dogfeeding.infra.exception.error;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Default API error response")
public record ErrorResponse(
        @Schema(description = "HTTP status", example = "400")
        int status,

        @Schema(description = "Error message", example = "Invalid data")
        String message,

        @Schema(description = "Timestamp when the error occurred")
        LocalDateTime timestamp,

        @Schema(description = "Validation errors by field")
        List<FieldError> errors
) {
    @Schema(description = "Validation error for a field")
    public record FieldError(
            @Schema(description = "Field name", example = "quantity")
            String field,

            @Schema(description = "Validation message", example = "Quantity must be positive")
            String message
    ) {}

    public ErrorResponse(int status, String message) {
        this(status, message, LocalDateTime.now(), List.of());
    }

    public ErrorResponse(int status, String message, List<FieldError> errors) {
        this(status, message, LocalDateTime.now(), errors);
    }
}
