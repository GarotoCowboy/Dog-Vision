package br.com.dogvision.dogmanagement.dto;

import br.com.dogvision.dogmanagement.model.Dog;
import br.com.dogvision.dogmanagement.model.enums.DogRace;
import br.com.dogvision.dogmanagement.model.enums.DogStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateDogRequest(
        @NotNull
        UUID ID,

        String name,
        DogRace race,
        DogStatus status,
        Character sex,

        @Min(value = 0, message = "the dogs age cannot be negative")
        Integer age
) {
        public void updateEntity(Dog dog) {
                if (this.name != null && !this.name.isBlank()) dog.setName(this.name);
                if (this.race != null) dog.setRace(this.race);
                if (this.status != null) dog.setStatus(this.status);
                if (this.sex != null) dog.setSex(this.sex);
                if (this.age != null) dog.setAge(this.age);

                dog.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        }
}