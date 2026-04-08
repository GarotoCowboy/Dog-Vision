package br.com.dogvision.dogmanagement.infra.exceptions.error;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Resposta de erro da API")
public record ErrorResponse(
        @Schema(description = "Código HTTP do erro", example = "400")
        int status,
        @Schema(description = "Mensagem do erro", example = "Validation Error")
        String message,
        @Schema(description = "Data e hora em que o erro ocorreu")
        LocalDateTime timestamp,
        @Schema(description = "Lista de erros por campo")
        List<FieldError> errors
) {
    @Schema(description = "Erro de validação de um campo")
    public record FieldError(
            @Schema(description = "Campo com erro", example = "name")
            String field,
            @Schema(description = "Mensagem de validação", example = "the dogs name is mandatory")
            String message
    ) {}

    public ErrorResponse(int status, String message) {
        this(status, message, LocalDateTime.now(), List.of());
    }

    public ErrorResponse(int status, String message, List<FieldError> errors) {
        this(status, message, LocalDateTime.now(), errors);
    }
}
