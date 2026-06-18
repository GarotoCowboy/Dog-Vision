package br.com.dogvision.dogfeeding.dto.response;

import br.com.dogvision.dogfeeding.model.MeasurementUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Ration item returned in feeding responses")
public record FeedingItemResponse(
        @Schema(description = "Ration UUID")
        UUID rationId,

        @Schema(description = "Ration name")
        String rationName,

        @Schema(description = "Ration type")
        String rationType,

        @Schema(description = "Quantity used from this ration")
        Double quantityUsed,

        @Schema(description = "Measurement unit")
        MeasurementUnit unit
) {
}
