package br.com.dogvision.dogfeeding.dto.update;

import br.com.dogvision.dogfeeding.model.MealType;
import br.com.dogvision.dogfeeding.model.MeasurementUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Schema(description = "Data to update a feeding plan")
public record UpdateFeedingPlanRequest(
        @Schema(description = "Ration UUID already registered by the coordinator", example = "550e8400-e29b-41d4-a716-446655440001")
        UUID rationId,

        @Schema(description = "Plan name", example = "Adult weight control")
        String name,

        @Schema(description = "Plan goal", example = "Weight maintenance with higher satiety")
        String goal,

        @Schema(description = "Daily recommended quantity", example = "0.75")
        @Positive(message = "Daily quantity must be positive")
        Double dailyQuantity,

        @Schema(description = "Measurement unit", example = "KILOGRAM")
        MeasurementUnit unit,

        @Schema(description = "Meal types planned for the dog")
        List<MealType> mealTypes,

        @Schema(description = "Plan notes", example = "Use this plan only on training weeks")
        @Size(max = 500, message = "Notes must have at most 500 characters")
        String notes,

        @Schema(description = "Plan start date", example = "2026-05-01")
        LocalDate startDate,

        @Schema(description = "Plan end date", example = "2026-06-30")
        LocalDate endDate,

        @Schema(description = "Whether the plan is active", example = "true")
        Boolean active
) {
}
