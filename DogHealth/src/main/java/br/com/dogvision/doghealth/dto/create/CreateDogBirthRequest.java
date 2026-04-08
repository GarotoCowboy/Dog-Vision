package br.com.dogvision.doghealth.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados para cadastrar um parto")
public record CreateDogBirthRequest(

        @Schema(description = "UUID da cadela", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "Dog id is required")
        UUID dogId,

        @Schema(description = "Nome da cadela no momento do parto", example = "Luna")
        @NotBlank(message = "Dog name is required")
        String dogsName,

        @Schema(description = "Raca da cadela no momento do parto", example = "Border Collie")
        @NotBlank(message = "Dog breed is required")
        String dogsBreed,

        @Schema(description = "Data e hora do parto", example = "2026-04-08T10:30:00")
        @NotNull
        LocalDateTime date,

        @Schema(description = "Quantidade de filhotes", example = "4")
        @Min(0)
        int numberOfPuppies,

        @Schema(description = "Inicio do parto", example = "2026-04-08T09:45:00")
        @NotNull(message = "Start time is required")
        LocalDateTime startTime,

        @Schema(description = "Fim do parto", example = "2026-04-08T11:20:00")
        LocalDateTime endTime
) {
}
