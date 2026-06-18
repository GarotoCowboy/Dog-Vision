package br.com.dogvision.doghealth.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Dados para atualizar uma massagem")
public record UpdateDogMassageRequest(
        @Schema(description = "Data e hora da massagem", example = "2026-04-08T10:30:00")
        LocalDateTime date,

        @Schema(description = "Observaçoes da massagem", example = "O cao nao reagiu bem a massagem")
        String observations
) {



}
