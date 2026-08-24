package br.com.dogvision.dogfeeding.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Data to increase ration stock by bag count and weight per bag")
public record IncreaseRationStockRequest(
        @Schema(description = "Number of bags", example = "3")
        @NotNull(message = "Bag count is required")
        @Positive(message = "Bag count must be positive")
        Integer bagCount,

        @Schema(description = "Weight of each bag in kilograms", example = "15.0")
        @NotNull(message = "Weight per bag is required")
        @Positive(message = "Weight per bag must be positive")
        Double weightPerBagKg
) {
    public double totalAddedWeightKg() {
        return bagCount * weightPerBagKg;
    }
}

