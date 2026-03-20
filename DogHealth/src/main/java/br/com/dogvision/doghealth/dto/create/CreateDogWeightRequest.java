package br.com.dogvision.doghealth.dto.create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreateDogWeightRequest(
        @NotNull(message = "Dog ID is required")
        UUID dogId,

        @NotNull(message = "Weight is required")
        @Positive(message = "Weight must be positive")
        Double weight
) {
}
