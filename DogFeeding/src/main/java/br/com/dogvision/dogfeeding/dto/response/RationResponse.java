package br.com.dogvision.dogfeeding.dto.response;

import br.com.dogvision.dogfeeding.model.RationStockStatus;
import br.com.dogvision.dogfeeding.model.RationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Response with ration data")
public record RationResponse(
        @Schema(description = "Ration UUID")
        UUID id,

        @Schema(description = "Ration name")
        String name,

        @Schema(description = "Ration type")
        RationType rationType,

        @Schema(description = "Total ration quantity in kilograms")
        Double totalRationQuantity,

        @Schema(description = "Current ration quantity in kilograms")
        Double currentRationQuantity,

        @Schema(description = "Ration registration date")
        LocalDate registrationDate,

        @Schema(description = "Current stock status")
        RationStockStatus stockStatus
) {
}
