package br.com.dogvision.doghealth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta com os dados de um parto")
public record DogBirthResponse(
        @Schema(description = "UUID do parto")
        UUID id,

        @Schema(description = "UUID do veterinario responsavel")
        UUID veterinarianId,

        @Schema(description = "UUID da cadela")
        UUID dogId,

        @Schema(description = "Nome da cadela no momento do parto")
        String dogsName,

        @Schema(description = "Raca da cadela no momento do parto")
        String dogsBreed,

        @Schema(description = "Data e hora do parto")
        LocalDateTime date,

        @Schema(description = "Quantidade de filhotes")
        int numberOfPuppies,

        @Schema(description = "Inicio do parto")
        LocalDateTime startTime,

        @Schema(description = "Fim do parto")
        LocalDateTime endTime
) {
}
