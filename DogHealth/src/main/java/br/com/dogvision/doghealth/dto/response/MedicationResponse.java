package br.com.dogvision.doghealth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta com os dados do medicamento")
public record MedicationResponse(
        @Schema(description = "UUID do medicamento")
        UUID id,

        @Schema(description = "Nome do cao")
        String dogsName,

        @Schema(description = "Prescricao do medicamento")
        String prescription,

        @Schema(description = "Data limite do medicamento")
        LocalDate limitDate,

        @Schema(description = "Data de criacao do registro")
        LocalDateTime createdAt,

        @Schema(description = "Data da ultima atualizacao do registro")
        LocalDateTime updatedAt
) {
}

