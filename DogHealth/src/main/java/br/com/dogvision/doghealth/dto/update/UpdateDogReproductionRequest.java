package br.com.dogvision.doghealth.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

@Schema(description = "Dados para atualização do controle reprodutivo da cadela")
public record UpdateDogReproductionRequest(

        @Schema(description = "Data do cio", example = "2026-04-01")
        LocalDate date,

        @Schema(description = "Data prevista do próximo cio", example = "2026-10-01")
        LocalDate expectedNextHeatDate,

        @Schema(description = "Intervalo em meses entre os cios", example = "6")
        @Min(value = 1, message = "Cycle interval must be at least 1 month")
        Integer cycleIntervalInMonths,

        @Schema(description = "Observações clínicas do cio", example = "Cio finalizado sem intercorrências")
        String observations
) {
}
