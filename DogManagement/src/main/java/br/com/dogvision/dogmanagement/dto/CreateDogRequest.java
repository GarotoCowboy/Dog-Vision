package br.com.dogvision.dogmanagement.dto;

import br.com.dogvision.dogmanagement.model.enums.DogRace;
import br.com.dogvision.dogmanagement.model.enums.DogStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

@Schema(description = "Dados para cadastrar um novo cão")
public record CreateDogRequest(
        @Schema(description = "Nome do cão", example = "Thor")
        @NotNull
        @NotBlank(message="the dogs name is mandatory")
        String name,

        @Schema(description = "Raça do cão", example = "Labrador")
        @NotNull(message = "dog race cannot be null")
        @Enumerated(EnumType.STRING)
        DogRace race,

        @Schema(description = "Status atual do cão no programa", example = "TREINAMENTO")
        @NotNull(message = "dog status cannot be null")
        @Enumerated(EnumType.STRING)
        DogStatus status,

        @Schema(description = "Sexo do cão", example = "M")
        @NotNull(message = "the dog sex cannot be null")
        Character sex,

        @Schema(description = "Idade do cão", example = "2", minimum = "0")
        @NotNull
        @Min(value = 0, message = "the dogs age cannot be negative")
        int age){

    }

