package br.com.dogvision.doghealth.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record DogBirthResponse(
        UUID id,

        UUID veterinarianId,

        UUID dogId,

        //DOG`S SNAPSHOT
        String dogsName,


        String dogsBreed,
//--------------------------------

        LocalDateTime date,

        int numberOfPuppies,

        LocalDateTime startTime,

        LocalDateTime endTime
) {
}
