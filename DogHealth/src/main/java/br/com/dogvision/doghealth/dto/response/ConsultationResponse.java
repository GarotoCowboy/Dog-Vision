package br.com.dogvision.doghealth.dto.response;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.UUID;

public record ConsultationResponse(

        UUID id,

        UUID veterinarianId,

        UUID dogId,

        String dogsName,

        String dogsBreed,

        String treatment,

        String diagnosis,

        LocalDate date
) {
}
