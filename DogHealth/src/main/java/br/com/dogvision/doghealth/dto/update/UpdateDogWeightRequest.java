package br.com.dogvision.doghealth.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

@Schema(description = "Dados para atualizar uma pesagem")
public record UpdateDogWeightRequest(
        @Schema(description = "Novo peso em quilogramas", example = "19.2")
        @Positive(message = "Weight must be positive")
        Double weight
) {
}
