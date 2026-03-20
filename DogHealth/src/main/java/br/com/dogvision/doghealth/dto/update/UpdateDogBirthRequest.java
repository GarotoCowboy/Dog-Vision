package br.com.dogvision.doghealth.dto.update;

import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

public record UpdateDogBirthRequest(

        LocalDateTime date,

        @Min(0)
        Integer numberOfPuppies,

        LocalDateTime startTime,

        LocalDateTime endTime
){
}
