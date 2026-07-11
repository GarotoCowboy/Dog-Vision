package br.com.dogvision.doghealth.dto.response;

import br.com.dogvision.doghealth.model.EnumUrgency;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta com os dados de uma cirurgia veterinaria")
public record DogSurgeryResponse(
        UUID id,
        UUID veterinarianId,
        UUID dogId,

        //DOG`S SNAPSHOT
        String dogsName,
        String dogsBreed,

        String title,
        LocalDateTime dateTimeOfSurgery,
        String durationExpected,
        EnumUrgency urgency,
        boolean onFasting,
        String observation,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
