package br.com.dogvision.doghealth.dto.create;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record CreateConsultationRequest(

        @NotNull(message = "Dog id is required") UUID dogId,

        @NotBlank @Size(min = 0, max = 256) String treatment,

        @NotBlank(message = "Dog name is required")
        String dogsName,

        @NotBlank(message = "Dog breed is required")
        String dogsBreed,

        @NotBlank(message = "Diagnosis is required")
        @Size(max = 256)
        String diagnosis,

        @NotNull
        LocalDate date
) {
}
