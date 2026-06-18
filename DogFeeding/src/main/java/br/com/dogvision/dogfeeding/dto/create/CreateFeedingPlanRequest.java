package br.com.dogvision.dogfeeding.dto.create;

import br.com.dogvision.dogfeeding.model.MealType;
import br.com.dogvision.dogfeeding.model.MeasurementUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Schema(description = "Data to create a feeding plan")
public record CreateFeedingPlanRequest(
        @Schema(description = "Dog UUID", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "Dog ID is required")
        UUID dogId,

        @Schema(description = "Ration UUID already registered by the coordinator", example = "550e8400-e29b-41d4-a716-446655440001")
        @NotNull(message = "Ration ID is required")
        UUID rationId,

        @Schema(description = "Plan name", example = "Adult weight control")
        @NotBlank(message = "Plan name is required")
        String name,

        @Schema(description = "Plan goal", example = "Weight maintenance with higher satiety")
        @NotBlank(message = "Goal is required")
        String goal,

        @Schema(description = "Daily recommended quantity", example = "0.70")
        @NotNull(message = "Daily quantity is required")
        @Positive(message = "Daily quantity must be positive")
        Double dailyQuantity,

        @Schema(description = "Measurement unit", example = "KILOGRAM")
        @NotNull(message = "Measurement unit is required")
        MeasurementUnit unit,

        @Schema(description = "Meal types planned for the dog")
        @NotEmpty(message = "At least one meal type is required")
        List<MealType> mealTypes,

        @Schema(description = "Plan notes", example = "Split meals evenly and avoid training before feeding")
        String notes,

        @Schema(description = "Plan start date", example = "2026-04-30")
        @NotNull(message = "Start date is required")
        LocalDate startDate,

        @Schema(description = "Plan end date", example = "2026-06-30")
        LocalDate endDate
) {
}
