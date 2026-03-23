package br.com.dogvision.user.dto.response;

import br.com.dogvision.user.model.EmployeeType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Dados de resposta de um monitor")
public record MonitorResponse(
        @Schema(description = "ID do funcionário", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID employeeId,
        @Schema(description = "ID do usuário", example = "550e8400-e29b-41d4-a716-446655440001")
        UUID userId,
        @Schema(description = "Matrícula de acesso ao sistema", example = "MON001")
        String registration,
        @Schema(description = "E-mail do monitor", example = "monitor@dogvision.com")
        String email,
        @Schema(description = "Nome completo do monitor", example = "Carlos Souza")
        String name,
        @Schema(description = "CPF do monitor", example = "12345678901")
        String cpf,
        @Schema(description = "Telefone do monitor", example = "11987654321")
        String phone,
        @Schema(description = "Tipo do funcionário", example = "MONITOR")
        EmployeeType type,
        @Schema(description = "Turno de trabalho do monitor", example = "MORNING")
        String shift
) {}