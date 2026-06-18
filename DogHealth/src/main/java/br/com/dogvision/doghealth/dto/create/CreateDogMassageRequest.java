package br.com.dogvision.doghealth.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados para cadastrar uma massagem")
public record CreateDogMassageRequest(

        @Schema(description = "UUID da cadela", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "Dog id is required")
        UUID dogId,

        @Schema(description = "Data e hora da massagem", example = "2026-04-08T10:30:00")
        @NotNull
        LocalDateTime date,

        @Schema(description = "Observaçoes do parto", example = "O cao nao reagiu bem a massagem")
        String observations

) {
}
