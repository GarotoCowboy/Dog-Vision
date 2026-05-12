package br.com.dogvision.dogmanagement.dto;

import br.com.dogvision.dogmanagement.model.enums.DogRace;
import br.com.dogvision.dogmanagement.model.enums.DogStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Dados para atualizar um cão")
public record UpdateDogRequest(
        @Schema(description = "ID do cão", example = "321956d9-8e0b-4269-9809-cc7e9909fe83")
        @NotNull
        UUID ID,

        @Schema(description = "Status atual do cão no programa", example = "TREINAMENTO")
        DogStatus status
) {
}
