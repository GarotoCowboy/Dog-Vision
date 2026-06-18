package br.com.dogvision.dogfeeding.dto.response;

import br.com.dogvision.dogfeeding.model.RationStockStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Operational alert for a ration")
public record RationAlertResponse(
        @Schema(description = "Ration UUID")
        UUID rationId,

        @Schema(description = "Ration name")
        String rationName,

        @Schema(description = "Current stock status")
        RationStockStatus stockStatus
) {
}
