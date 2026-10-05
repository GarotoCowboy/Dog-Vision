package br.com.dogvision.dogtraining.dto.update;

import java.util.UUID;

public record UpdateTrainingRequest(
        UUID id,
        UUID trainerId,
        UUID dogId,
        String dogsName,
        String dogsBreed,
        Integer monthYear,
        Integer day,
        String evaluationType
) {}