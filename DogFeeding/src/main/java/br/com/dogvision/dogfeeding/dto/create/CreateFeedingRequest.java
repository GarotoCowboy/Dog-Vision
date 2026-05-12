package br.com.dogvision.dogfeeding.dto.create;

import br.com.dogvision.dogfeeding.model.MealType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Data to create a feeding")
public record CreateFeedingRequest(
        @Schema(description = "Dog UUID", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "Dog ID is required")
        UUID dogId,

        @Schema(description = "Feeding date", example = "2026-04-30")
        @NotNull(message = "Date is required")
        LocalDate date,

        @Schema(description = "Feeding time", example = "08:30")
        @NotNull(message = "Feeding time is required")
        LocalTime feedingTime,

        @Schema(description = "Meal type", example = "BREAKFAST")
        @NotNull(message = "Meal type is required")
        MealType mealType,

        @Schema(description = "Ration items used in feeding")
        @NotEmpty(message = "At least one feeding item is required")
        @Valid
        List<CreateFeedingItemRequest> items,

        @Schema(description = "Internal notes about the feeding", example = "Dog was calm and finished all food")
        @Size(max = 500, message = "Notes must have at most 500 characters")
        String notes,

        @Schema(description = "Observed dog response to the meal", example = "Ate well and remained active")
        @Size(max = 500, message = "Dog response must have at most 500 characters")
        String dogResponse
) {
}
