package br.com.dogvision.dogfeeding.dto.update;

import br.com.dogvision.dogfeeding.model.MealType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Schema(description = "Data to update a feeding")
public record UpdateFeedingRequest(
        @Schema(description = "Feeding date", example = "2026-04-30")
        LocalDate date,

        @Schema(description = "Feeding time", example = "09:00")
        LocalTime feedingTime,

        @Schema(description = "Meal type", example = "DINNER")
        MealType mealType,

        @Schema(description = "Ration items used in feeding")
        @Valid
        List<UpdateFeedingItemRequest> items,

        @Schema(description = "Internal notes about the feeding", example = "Required a slower feeder bowl")
        @Size(max = 500, message = "Notes must have at most 500 characters")
        String notes,

        @Schema(description = "Observed dog response to the meal", example = "Needed more water after meal")
        @Size(max = 500, message = "Dog response must have at most 500 characters")
        String dogResponse
) {
}
