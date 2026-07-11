package br.com.dogvision.doghealth.dto.update;

import br.com.dogvision.doghealth.model.EnumUrgency;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados para atualizar uma cirurgia veterinaria")
public record UpdateDogSurgeryRequest(
        UUID dogId,

        LocalDateTime dateTimeOfSurgery,
        String durationExpected,
        EnumUrgency urgency,
        boolean onFasting,
        String observation
) {
}
