package br.com.dogvision.doghealth.dto.create;

import br.com.dogvision.doghealth.model.EnumUrgency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados para cadastrar uma cirurgia veterinaria")
public record CreateDogSurgeryRequest(
        @Schema(description = "UUID do cao que passara pela cirurgia", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "Dog id is required")
        UUID dogId,

        @Schema(description = "Nome do cao no momento do cadastro", example = "Thor")
        @NotBlank(message = "Dog name is required")
        String dogsName,

        @Schema(description = "Raca do cao no momento do cadastro", example = "Golden Retriever")
        @NotBlank(message = "Dog breed is required")
        String dogsBreed,

        @Schema(description = "Titulo da cirurgia", example = "Remocao de tumor benigno")
        @NotBlank(message = "Title is required")
        String title,

        @Schema(description = "Data e hora previstas da cirurgia", example = "2026-05-17T14:30:00")
        @NotNull(message = "Date and time of surgery is required")
        LocalDateTime dateTimeOfSurgery,

        @Schema(description = "Duracao esperada da cirurgia", example = "2 horas")
        @NotBlank(message = "Expected duration is required")
        String durationExpected,

        @Schema(description = "Nivel de urgencia", example = "NORMAL")
        @NotNull(message = "Urgency is required")
        EnumUrgency urgency,

        @Schema(description = "Indica se o cao deve estar em jejum", example = "true")
        boolean onFasting,

        @Schema(description = "Observacoes adicionais", example = "Jejum de 8 horas antes do procedimento")
        @NotBlank(message = "Observation is required")
        String observation
) {
}
