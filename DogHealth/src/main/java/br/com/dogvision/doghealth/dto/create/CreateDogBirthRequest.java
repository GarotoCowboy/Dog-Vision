package br.com.dogvision.doghealth.dto.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateDogBirthRequest(

        @NotNull(message = "Dog id is required")
        UUID dogId,

        //DOG`S SNAPSHOT
        @NotBlank(message = "Dog name is required")
        String dogsName,

        @NotBlank(message = "Dog breed is required")
        String dogsBreed,
//--------------------------------

        @NotNull
        LocalDateTime date,


        @Min(0)
        int numberOfPuppies,

        @NotNull(message = "Start time is required")
        LocalDateTime startTime,

        LocalDateTime endTime

){



}
