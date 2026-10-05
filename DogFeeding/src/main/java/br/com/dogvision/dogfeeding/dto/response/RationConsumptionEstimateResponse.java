package br.com.dogvision.dogfeeding.dto.response;

import br.com.dogvision.dogfeeding.model.RationStockStatus;
import br.com.dogvision.dogfeeding.model.RationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Schema(description = "Ration consumption estimate and stock duration")
public record RationConsumptionEstimateResponse(
        @Schema(description = "Ration UUID")
        UUID rationId,

        @Schema(description = "Ration name")
        String rationName,

        @Schema(description = "Ration type")
        RationType rationType,

        @Schema(description = "Current ration quantity in kilograms")
        Double currentRationQuantityKg,

        @Schema(description = "Total daily consumption across all dogs in kilograms")
        Double totalDailyConsumptionKg,

        @Schema(description = "Estimated days remaining until stock is depleted")
        Double estimatedDaysRemaining,

        @Schema(description = "Estimated depletion date based on current daily consumption")
        LocalDate estimatedDepletionDate,

        @Schema(description = "Current stock status")
        RationStockStatus stockStatus,

        @Schema(description = "List of dog consumption estimates for this ration")
        List<DogRationConsumptionResponse> dogConsumptions,

        @Schema(description = "Date of last daily deduction")
        LocalDate lastDailyDeductionDate,

        @Schema(description = "Whether daily deduction was already applied today")
        Boolean dailyDeductionAppliedToday
) {
    public RationConsumptionEstimateResponse(
            UUID rationId,
            String rationName,
            RationType rationType,
            Double currentRationQuantityKg,
            Double totalDailyConsumptionKg,
            Double estimatedDaysRemaining,
            LocalDate estimatedDepletionDate,
            RationStockStatus stockStatus,
            List<DogRationConsumptionResponse> dogConsumptions
    ) {
        this(rationId, rationName, rationType, currentRationQuantityKg, totalDailyConsumptionKg, estimatedDaysRemaining, estimatedDepletionDate, stockStatus, dogConsumptions, null, false);
    }

    public Double dailyConsumptionKg() {
        return totalDailyConsumptionKg;
    }
}

