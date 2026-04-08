package br.com.dogvision.doghealth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta com os dados de uma pesagem")
public record DogWeightResponse(
        @Schema(description = "UUID da pesagem")
        UUID id,

        @Schema(description = "UUID do cao pesado")
        UUID dogId,

        @Schema(description = "UUID do monitor responsavel")
        UUID monitorId,

        @Schema(description = "Peso em quilogramas")
        Double weight,

        @Schema(description = "Data de criacao do registro")
        LocalDateTime createdAt,

        @Schema(description = "Data da ultima atualizacao do registro")
        LocalDateTime updatedAt
) {
}
