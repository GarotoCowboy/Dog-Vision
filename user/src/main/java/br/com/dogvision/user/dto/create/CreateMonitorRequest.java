package br.com.dogvision.user.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados para cadastro de um novo monitor")
public record CreateMonitorRequest(

        @Schema(description = "E-mail do monitor", example = "monitor@dogvision.com")
        @NotBlank @Email String email,

        @Schema(description = "Nome completo do monitor", example = "Carlos Souza")
        @NotBlank String name,

        @Schema(description = "CPF do monitor (apenas números, 11 dígitos)", example = "12345678901")
        @NotBlank @Size(min = 11, max = 11) String cpf,

        @Schema(description = "Telefone do monitor (9 a 11 dígitos)", example = "11987654321")
        @NotBlank @Size(min = 9, max = 11) String phone,

        @Schema(description = "Matrícula de acesso ao sistema", example = "MON001")
        @NotBlank String registration,

        @Schema(description = "Senha de acesso (mínimo 8, máximo 60 caracteres)", example = "senha@123")
        @NotBlank @Size(min = 8, max = 60) String password,

        @Schema(description = "Turno de trabalho do monitor", example = "MORNING")
        @NotBlank String shift

) {}