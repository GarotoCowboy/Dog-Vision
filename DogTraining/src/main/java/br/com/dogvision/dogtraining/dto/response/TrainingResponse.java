package br.com.dogvision.dogtraining.dto.response;

import br.com.dogvision.dogtraining.model.enums.TraningStatus;

import java.util.UUID;

public record TrainingResponse(
        UUID id,
        UUID trainerId,
        UUID dogId,
        String dogsName,
        String dogsBreed,
        int monthYear,
        int day,
        String evaluationType,
        int currentStage,
        TraningStatus status
) {
}