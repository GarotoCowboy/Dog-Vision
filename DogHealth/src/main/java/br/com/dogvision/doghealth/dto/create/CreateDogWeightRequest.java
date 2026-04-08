package br.com.dogvision.doghealth.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

@Schema(description = "Dados para cadastrar uma pesagem")
public record CreateDogWeightRequest(
        @Schema(description = "UUID do cao pesado", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "Dog ID is required")
        UUID dogId,

        @Schema(description = "Peso em quilogramas", example = "18.7")
        @NotNull(message = "Weight is required")
        @Positive(message = "Weight must be positive")
        Double weight
) {
}
