package br.com.dogvision.dogfeeding.dto.create;

import br.com.dogvision.dogfeeding.model.MeasurementUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

@Schema(description = "Ration item used in a feeding")
public record CreateFeedingItemRequest(
        @Schema(description = "Ration UUID", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "Ration ID is required")
        UUID rationId,

        @Schema(description = "Quantity used from this ration", example = "0.20")
        @NotNull(message = "Quantity used is required")
        @Positive(message = "Quantity used must be positive")
        Double quantityUsed,

        @Schema(description = "Measurement unit", example = "KILOGRAM")
        @NotNull(message = "Measurement unit is required")
        MeasurementUnit unit
) {
}
