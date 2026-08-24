package br.com.dogvision.notification.infra.exception.error;

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
            @Schema(description = "Field name", example = "title")
            String field,

            @Schema(description = "Validation message", example = "Title cannot be blank")
            String message
    ) {}

    public ErrorResponse(int status, String message) {
        this(status, message, LocalDateTime.now(), List.of());
    }

    public ErrorResponse(int status, String message, List<FieldError> errors) {
        this(status, message, LocalDateTime.now(), errors);
    }
}
