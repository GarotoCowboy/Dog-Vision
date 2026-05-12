package br.com.dogvision.dogfeeding.dto.create;

import br.com.dogvision.dogfeeding.model.RationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Schema(description = "Data to create a ration")
public record CreateRationRequest(
        @Schema(description = "Ration name", example = "Premium Adult Chicken")
        @NotBlank(message = "Ration name is required")
        String name,

        @Schema(description = "Ration type", example = "NORMAL")
        @NotNull(message = "Ration type is required")
        RationType rationType,

        @Schema(description = "Total ration quantity in kilograms", example = "15.0")
        @NotNull(message = "Total ration quantity is required")
        @Positive(message = "Total ration quantity must be positive")
        Double totalRationQuantity,

        @Schema(description = "Current ration quantity in kilograms", example = "12.5")
        @NotNull(message = "Current ration quantity is required")
        @Positive(message = "Current ration quantity must be positive")
        Double currentRationQuantity,

        @Schema(description = "Ration registration date", example = "2026-04-30")
        @NotNull(message = "Registration date is required")
        LocalDate registrationDate
) {
}
