package br.com.dogvision.doghealth.dto.update;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateConsultationRequest(
        @Size(max = 256)
        String treatment,
         @Size(max = 256)
        String diagnosis
) {
}
