package br.com.dogvision.user.dto.response;

import br.com.dogvision.user.model.EmployeeType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Dados de resposta de um veterinário")
public record VeterinarianResponse(
        @Schema(description = "ID do funcionário", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID employeeId,
        @Schema(description = "ID do usuário", example = "550e8400-e29b-41d4-a716-446655440001")
        UUID userId,
        @Schema(description = "Matrícula de acesso ao sistema", example = "VET001")
        String registration,
        @Schema(description = "E-mail do veterinário", example = "veterinario@dogvision.com")
        String email,
        @Schema(description = "Nome completo do veterinário", example = "Ana Costa")
        String name,
        @Schema(description = "CPF do veterinário", example = "12345678901")
        String cpf,
        @Schema(description = "Telefone do veterinário", example = "11987654321")
        String phone,
        @Schema(description = "Tipo do funcionário", example = "VETERINARIAN")
        EmployeeType type,
        @Schema(description = "Número do CRMV do veterinário", example = "SP-12345")
        String crmv,
        @Schema(description = "Área de especialização do veterinário", example = "Clínica geral")
        String areaOfExpertise
) {}