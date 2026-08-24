package br.com.dogvision.dogfeeding.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Data to decrease ration stock quantity")
public record DecreaseRationStockRequest(
        @Schema(description = "Quantity in kilograms to decrease", example = "2.5")
        @NotNull(message = "Quantity to decrease is required")
        @Positive(message = "Quantity to decrease must be positive")
        Double quantityKg
) {
}

