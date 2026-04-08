package br.com.dogvision.doghealth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Resposta com os dados de uma consulta veterinaria")
public record ConsultationResponse(

        @Schema(description = "UUID da consulta")
        UUID id,

        @Schema(description = "UUID do veterinario responsavel")
        UUID veterinarianId,

        @Schema(description = "UUID do cao consultado")
        UUID dogId,

        @Schema(description = "Nome do cao no momento da consulta")
        String dogsName,

        @Schema(description = "Raca do cao no momento da consulta")
        String dogsBreed,

        @Schema(description = "Tratamento indicado")
        String treatment,

        @Schema(description = "Diagnostico da consulta")
        String diagnosis,

        @Schema(description = "Data da consulta")
        LocalDate date
) {
}
