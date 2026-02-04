package br.com.dogvision.dogmanagement.dto;

import br.com.dogvision.dogmanagement.model.Dog;
import br.com.dogvision.dogmanagement.model.enums.DogRace;
import br.com.dogvision.dogmanagement.model.enums.DogStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;


public record CreateDogRequest(
        @NotNull
        @NotBlank(message="the dogs name is mandatory")
        String name,

        @NotNull
        @Enumerated(EnumType.STRING)
        DogRace race,

        @NotNull
        @Enumerated(EnumType.STRING)
        DogStatus status,

        @NotNull
        Character sex,

        @NotNull
        @Min(value = 0, message = "the dogs age cannot be negative")
        int age){

        public Dog toEntity(){
                Dog dog = new Dog();
                dog.setName(this.name);
                dog.setAge(this.age);
                dog.setSex(this.sex);
                dog.setRace(this.race);
                dog.setStatus(this.status);
                return dog;
        }
    }

