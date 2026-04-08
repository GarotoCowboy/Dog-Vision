package br.com.dogvision.dogmanagement.dto;

import br.com.dogvision.dogmanagement.model.enums.DogRace;
import br.com.dogvision.dogmanagement.model.enums.DogStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

@Schema(description = "Dados retornados de um cão")
public record DogResponse(
                          @Schema(description = "Nome do cão", example = "Thor")
                          String name,
                          @Schema(description = "Raça do cão", example = "Labrador")
                          DogRace race,
                          @Schema(description = "Status atual do cão no programa", example = "TREINAMENTO")
                          DogStatus status,
                          @Schema(description = "Sexo do cão", example = "M")
                          Character sex,
                          @Schema(description = "Idade do cão", example = "2")
                          int age,
                          @Schema(description = "Data de criação do registro")
                          Timestamp createdAt,
                          @Schema(description = "Data da última atualização do registro")
                          Timestamp updatedAt){


}
