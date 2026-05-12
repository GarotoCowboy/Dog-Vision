package br.com.dogvision.doghealth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta com os dados de uma massagem")
public record DogMassageResponse(

        @Schema(description = "UUID da consulta")
        UUID id,

        @Schema(description = "UUID da cadela")
        @NotNull(message = "Dog id is required")
        UUID dogId,

        @Schema(description = "Observaçoes do parto")
        String observations,

        @Schema(description = "Data de criacao do registro")
        LocalDateTime createdAt,

        @Schema(description = "Data da ultima atualizacao do registro")
        LocalDateTime updatedAt
){
}
