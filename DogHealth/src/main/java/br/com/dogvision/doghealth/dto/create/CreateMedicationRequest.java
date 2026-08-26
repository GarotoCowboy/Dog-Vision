package br.com.dogvision.doghealth.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "Dados para cadastrar uma prescricao/gestao de medicamento")
public record CreateMedicationRequest(
        @Schema(description = "Nome do cao", example = "Thor")
        @NotBlank(message = "Dog name is required")
        String dogsName,

        @Schema(description = "Prescricao do medicamento", example = "Amoxicilina 250mg a cada 12h")
        @NotBlank(message = "Prescription is required")
        String prescription,

        @Schema(description = "Data limite para administracao do medicamento", example = "2026-06-30")
        @NotNull(message = "Limit date is required")
        LocalDate limitDate
) {
}

