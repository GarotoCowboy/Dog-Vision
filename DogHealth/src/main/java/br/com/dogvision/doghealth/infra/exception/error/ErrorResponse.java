package br.com.dogvision.doghealth.infra.exception.error;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Resposta padrao para erros da API")
public record ErrorResponse(
        @Schema(description = "Status HTTP", example = "400")
        int status,

        @Schema(description = "Mensagem do erro", example = "Dados invalidos")
        String message,

        @Schema(description = "Data e hora em que o erro ocorreu")
        LocalDateTime timestamp,

        @Schema(description = "Erros de validacao por campo")
        List<FieldError> errors
) {
    @Schema(description = "Erro de validacao de um campo")
    public record FieldError(
            @Schema(description = "Nome do campo", example = "weight")
            String field,

            @Schema(description = "Mensagem de validacao", example = "Weight must be positive")
            String message
    ) {}

    public ErrorResponse(int status, String message) {
        this(status, message, LocalDateTime.now(), List.of());
    }

    public ErrorResponse(int status, String message, List<FieldError> errors) {
        this(status, message, LocalDateTime.now(), errors);
    }
}
