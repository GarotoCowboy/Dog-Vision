package br.com.dogvision.user.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados para cadastro de um novo veterinário")
public record CreateVeterinarianRequest(

        @Schema(description = "E-mail do veterinário", example = "veterinario@dogvision.com")
        @NotBlank @Email String email,

        @Schema(description = "Nome completo do veterinário", example = "Ana Costa")
        @NotBlank String name,

        @Schema(description = "CPF do veterinário (apenas números, 11 dígitos)", example = "12345678901")
        @NotBlank @Size(min = 11, max = 11) String cpf,

        @Schema(description = "Telefone do veterinário (9 a 11 dígitos)", example = "11987654321")
        @NotBlank @Size(min = 9, max = 11) String phone,

        @Schema(description = "Matrícula de acesso ao sistema", example = "VET001")
        @NotBlank String registration,

        @Schema(description = "Senha de acesso (mínimo 8, máximo 60 caracteres)", example = "senha@123")
        @NotBlank @Size(min = 8, max = 60) String password,

        @Schema(description = "Número do CRMV do veterinário", example = "SP-12345")
        @NotBlank String crmv,

        @Schema(description = "Área de especialização do veterinário", example = "Clínica geral")
        @NotBlank String areaOfExpertise

) {}