package br.com.dogvision.doghealth.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Dados para cadastrar uma consulta veterinaria")
public record CreateConsultationRequest(

        @Schema(description = "UUID do cao consultado", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "Dog id is required") UUID dogId,

        @Schema(description = "Tratamento indicado na consulta", example = "Administrar antibiotico por 7 dias")
        @NotBlank @Size(min = 0, max = 256) String treatment,

        @Schema(description = "Nome do cao no momento da consulta", example = "Thor")
        @NotBlank(message = "Dog name is required")
        String dogsName,

        @Schema(description = "Raca do cao no momento da consulta", example = "Golden Retriever")
        @NotBlank(message = "Dog breed is required")
        String dogsBreed,

        @Schema(description = "Diagnostico da consulta", example = "Dermatite alergica")
        @NotBlank(message = "Diagnosis is required")
        @Size(max = 256)
        String diagnosis,

        @Schema(description = "Data da consulta", example = "2026-04-08")
        @NotNull
        LocalDate date
) {
}
