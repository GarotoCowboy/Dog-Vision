package br.com.dogvision.doghealth.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Dados para cadastro de controle reprodutivo da cadela")
public record CreateDogReproductionRequest(

        @Schema(description = "UUID da cadela", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "Dog id is required")
        UUID dogId,

        @Schema(description = "Nome da cadela no momento do registro", example = "Luna")
        @NotBlank(message = "Dog name is required")
        String dogsName,

        @Schema(description = "Raça da cadela no momento do registro", example = "Labrador Retriever")
        @NotBlank(message = "Dog breed is required")
        String dogsBreed,

        @Schema(description = "Data do cio observado", example = "2026-04-01")
        @NotNull(message = "Date is required")
        LocalDate date,

        @Schema(description = "Data prevista do próximo cio. Se omitida, calcula-se com base no intervalo (padrão de 6 meses)", example = "2026-10-01")
        LocalDate expectedNextHeatDate,

        @Schema(description = "Intervalo em meses entre os cios (padrão de 6 meses)", example = "6")
        @Min(value = 1, message = "Cycle interval must be at least 1 month")
        Integer cycleIntervalInMonths,

        @Schema(description = "Observações clínicas do cio", example = "Cio iniciado normalmente, sem corrimentos atípicos")
        String observations
) {
}
