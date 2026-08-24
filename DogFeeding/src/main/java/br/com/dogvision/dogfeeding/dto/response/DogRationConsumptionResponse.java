package br.com.dogvision.dogfeeding.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Dog consumption estimate detail for a ration")
public record DogRationConsumptionResponse(
        @Schema(description = "Dog UUID")
        UUID dogId,

        @Schema(description = "Feeding plan UUID")
        UUID feedingPlanId,

        @Schema(description = "Feeding plan name")
        String feedingPlanName,

        @Schema(description = "Estimated daily consumption in kilograms")
        Double dailyQuantityKg
) {
}

