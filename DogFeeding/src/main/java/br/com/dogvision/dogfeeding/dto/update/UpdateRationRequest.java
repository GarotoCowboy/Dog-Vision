package br.com.dogvision.dogfeeding.dto.update;

import br.com.dogvision.dogfeeding.model.RationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Data to update a ration")
public record UpdateRationRequest(
        @Schema(description = "Ration name", example = "Premium Adult Chicken")
        String name,

        @Schema(description = "Ration type", example = "SPECIAL")
        RationType rationType,

        @Schema(description = "Ration registration date", example = "2026-04-30")
        LocalDate registrationDate
) {
}
