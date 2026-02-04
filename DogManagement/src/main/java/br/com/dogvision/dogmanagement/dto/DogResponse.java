package br.com.dogvision.dogmanagement.dto;

import br.com.dogvision.dogmanagement.model.Dog;
import br.com.dogvision.dogmanagement.model.enums.DogRace;
import br.com.dogvision.dogmanagement.model.enums.DogStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

public record DogResponse(String name,
                          DogRace race,
                          DogStatus status,
                          Character sex,
                          int age,
                          Timestamp createdAt,
                          Timestamp updatedAt) {

    public DogResponse(Dog dog) {
        this(
                dog.getName(),
                dog.getRace(),
                dog.getStatus(),
                dog.getSex(),
                dog.getAge(),
                dog.getCreatedAt(),
                dog.getUpdatedAt()
        );
    }

}
