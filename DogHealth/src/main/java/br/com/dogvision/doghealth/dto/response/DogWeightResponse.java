package br.com.dogvision.doghealth.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record DogWeightResponse(
        UUID id,
        UUID dogId,
        UUID monitorId,
        Double weight,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
