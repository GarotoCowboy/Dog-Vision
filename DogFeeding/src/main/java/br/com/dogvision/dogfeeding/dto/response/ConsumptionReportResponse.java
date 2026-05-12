package br.com.dogvision.dogfeeding.dto.response;

import br.com.dogvision.dogfeeding.model.MeasurementUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Dog consumption report")
public record ConsumptionReportResponse(
        @Schema(description = "Dog UUID")
        UUID dogId,

        @Schema(description = "Ration UUID")
        UUID rationId,

        @Schema(description = "Ration name")
        String rationName,

        @Schema(description = "Total quantity consumed")
        Double totalConsumed,

        @Schema(description = "Measurement unit")
        MeasurementUnit unit,

        @Schema(description = "Initial report date")
        LocalDate startDate,

        @Schema(description = "Final report date")
        LocalDate endDate
) {
}
