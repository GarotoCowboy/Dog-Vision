package br.com.dogvision.dogfeeding.dto.response;

import br.com.dogvision.dogfeeding.model.MealType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Response with feeding data")
public record FeedingResponse(
        @Schema(description = "Feeding UUID")
        UUID id,

        @Schema(description = "Dog UUID")
        UUID dogId,

        @Schema(description = "Feeding date")
        LocalDate date,

        @Schema(description = "Feeding time")
        LocalTime feedingTime,

        @Schema(description = "Meal type")
        MealType mealType,

        @Schema(description = "Ration items used in feeding")
        List<FeedingItemResponse> items,

        @Schema(description = "Total quantity served in kilograms")
        Double totalQuantity,

        @Schema(description = "Internal notes about the feeding")
        String notes,

        @Schema(description = "Observed dog response to the meal")
        String dogResponse,

        @Schema(description = "Creation date of the record")
        LocalDateTime createdAt,

        @Schema(description = "Last update date of the record")
        LocalDateTime updatedAt
) {
}
