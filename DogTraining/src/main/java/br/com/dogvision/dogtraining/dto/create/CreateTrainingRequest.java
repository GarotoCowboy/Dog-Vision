package br.com.dogvision.dogtraining.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateTrainingRequest(
        @NotNull(message = "Trainer ID is required")
        UUID trainerId,

        @NotNull(message = "Dog ID is required")
        UUID dogId,

        @NotBlank(message = "Dog's name is required")
        String dogsName,

        @NotBlank(message = "Dog's breed is required")
        String dogsBreed,

        @NotNull(message = "Training month/year is required")
        Integer monthYear,

        @NotNull(message = "Training day is required")
        Integer day,

        @NotBlank(message = "Evaluation type is required")
        @Size(max = 256, message = "Evaluation type must be up to 256 characters")
        String evaluationType
) {}