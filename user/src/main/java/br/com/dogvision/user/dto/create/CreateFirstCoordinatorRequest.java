package br.com.dogvision.user.dto.create;

import br.com.dogvision.user.model.ShiftEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro inicial do primeiro coordenador do sistema")
public record CreateFirstCoordinatorRequest(

        @Schema(description = "E-mail do coordenador", example = "coordinator@dogvision.com")
        @NotBlank @Email String email,

        @Schema(description = "Nome completo do coordenador", example = "João Silva")
        @NotBlank String name,

        @Schema(description = "Telefone do coordenador (9 a 11 dígitos)", example = "11987654321")
        @NotBlank @Size(min = 9, max = 11) String phone,

        @Schema(description = "Matrícula de acesso ao sistema", example = "COORD001")
        @NotBlank String registration,

        @Schema(description = "Senha de acesso inicial (mínimo 8, máximo 60 caracteres)", example = "admin@123")
        @NotBlank @Size(min = 8, max = 60) String password,

        @Schema(description = "Turno de trabalho", example = "MORNING")
        @NotNull ShiftEnum shift

) {}
