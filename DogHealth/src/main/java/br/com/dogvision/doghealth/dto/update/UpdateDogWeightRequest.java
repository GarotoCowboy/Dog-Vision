package br.com.dogvision.doghealth.dto.update;

import jakarta.validation.constraints.Positive;

public record UpdateDogWeightRequest(
        @Positive(message = "Weight must be positive")
        Double weight
) {
}
