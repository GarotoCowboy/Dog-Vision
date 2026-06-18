package br.com.dogvision.dogfeeding.dto.update;

import br.com.dogvision.dogfeeding.model.RationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Schema(description = "Data to update a ration")
public record UpdateRationRequest(
        @Schema(description = "Ration name", example = "Premium Adult Chicken")
        String name,

        @Schema(description = "Ration type", example = "SPECIAL")
        RationType rationType,

        @Schema(description = "Total ration quantity in kilograms", example = "20.0")
        @Positive(message = "Total ration quantity must be positive")
        Double totalRationQuantity,

        @Schema(description = "Current ration quantity in kilograms", example = "10.0")
        @Positive(message = "Current ration quantity must be positive")
        Double currentRationQuantity,

        @Schema(description = "Ration registration date", example = "2026-04-30")
        LocalDate registrationDate
) {
}
