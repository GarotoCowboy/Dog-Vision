package br.com.dogvision.doghealth.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualizar uma consulta veterinaria")
public record UpdateConsultationRequest(
        @Schema(description = "Novo tratamento indicado", example = "Manter repouso e medicacao por 5 dias")
        @Size(max = 256)
        String treatment,

        @Schema(description = "Novo diagnostico", example = "Quadro inflamatorio controlado")
        @Size(max = 256)
        String diagnosis
) {
}
