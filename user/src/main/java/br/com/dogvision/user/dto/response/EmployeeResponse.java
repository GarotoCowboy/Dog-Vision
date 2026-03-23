package br.com.dogvision.user.dto.response;

import br.com.dogvision.user.model.EmployeeType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Dados de resposta de um funcionário")
public record EmployeeResponse(
        @Schema(description = "ID do funcionário", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID employeeId,
        @Schema(description = "ID do usuário", example = "550e8400-e29b-41d4-a716-446655440001")
        UUID userId,
        @Schema(description = "Matrícula de acesso ao sistema", example = "FUNC001")
        String registration,
        @Schema(description = "E-mail do funcionário", example = "funcionario@dogvision.com")
        String email,
        @Schema(description = "Nome completo do funcionário", example = "Maria Oliveira")
        String name,
        @Schema(description = "CPF do funcionário", example = "12345678901")
        String cpf,
        @Schema(description = "Telefone do funcionário", example = "11987654321")
        String phone,
        @Schema(description = "Tipo do funcionário", example = "VETERINARIAN")
        EmployeeType type
) {}