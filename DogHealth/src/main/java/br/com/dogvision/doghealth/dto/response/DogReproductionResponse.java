package br.com.dogvision.doghealth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta com dados do controle reprodutivo da cadela")
public record DogReproductionResponse(

        @Schema(description = "UUID do registro de controle reprodutivo")
        UUID id,

        @Schema(description = "UUID da cadela")
        UUID dogId,

        @Schema(description = "Nome da cadela no momento do registro")
        String dogsName,

        @Schema(description = "Raça da cadela no momento do registro")
        String dogsBreed,

        @Schema(description = "UUID do veterinário responsável pelo registro")
        UUID veterinarianId,

        @Schema(description = "Data do cio")
        LocalDate date,

        @Schema(description = "Data prevista do próximo cio")
        LocalDate expectedNextHeatDate,

        @Schema(description = "Intervalo em meses entre os cios")
        Integer cycleIntervalInMonths,

        @Schema(description = "Observações clínicas do cio")
        String observations,

        @Schema(description = "Data e hora de criação do registro")
        LocalDateTime createdAt,

        @Schema(description = "Data e hora da última atualização do registro")
        LocalDateTime updatedAt
) {
}
