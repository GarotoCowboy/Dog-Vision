package br.com.dogvision.dogfeeding.dto.response;

import br.com.dogvision.dogfeeding.model.MealType;
import br.com.dogvision.dogfeeding.model.MeasurementUnit;
import br.com.dogvision.dogfeeding.model.RationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Response with feeding plan data")
public record FeedingPlanResponse(
        @Schema(description = "Plan UUID")
        UUID id,

        @Schema(description = "Dog UUID")
        UUID dogId,

        @Schema(description = "Ration UUID selected for the plan")
        UUID rationId,

        @Schema(description = "Ration name selected for the plan")
        String rationName,

        @Schema(description = "Ration type selected for the plan")
        RationType rationType,

        @Schema(description = "Plan name")
        String name,

        @Schema(description = "Plan goal")
        String goal,

        @Schema(description = "Daily quantity")
        Double dailyQuantity,

        @Schema(description = "Measurement unit")
        MeasurementUnit unit,

        @Schema(description = "Meal types")
        List<MealType> mealTypes,

        @Schema(description = "Plan notes")
        String notes,

        @Schema(description = "Plan start date")
        LocalDate startDate,

        @Schema(description = "Plan end date")
        LocalDate endDate,

        @Schema(description = "Whether the plan is active")
        Boolean active,

        @Schema(description = "Creation date of the record")
        LocalDateTime createdAt,

        @Schema(description = "Last update date of the record")
        LocalDateTime updatedAt
) {
}
