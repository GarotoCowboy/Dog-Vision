package br.com.dogvision.doghealth.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

@Schema(description = "Dados para atualizar um parto")
public record UpdateDogBirthRequest(

        @Schema(description = "Nova data e hora do parto", example = "2026-04-08T10:30:00")
        LocalDateTime date,

        @Schema(description = "Nova quantidade de filhotes", example = "4")
        @Min(0)
        Integer numberOfPuppies,

        @Schema(description = "Novo horario de inicio", example = "2026-04-08T09:45:00")
        LocalDateTime startTime,

        @Schema(description = "Novo horario de fim", example = "2026-04-08T11:20:00")
        LocalDateTime endTime
) {
}
