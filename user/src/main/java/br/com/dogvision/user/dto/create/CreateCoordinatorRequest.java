package br.com.dogvision.user.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados para cadastro de um novo coordenador")
public record CreateCoordinatorRequest(

        @Schema(description = "E-mail do coordenador", example = "coordenador@dogvision.com")
        @NotBlank @Email String email,

        @Schema(description = "Nome completo do coordenador", example = "João da Silva")
        @NotBlank String name,

        @Schema(description = "CPF do coordenador (apenas números, 11 dígitos)", example = "12345678901")
        @NotBlank @Size(min = 11, max = 11) String cpf,

        @Schema(description = "Telefone do coordenador (9 a 11 dígitos)", example = "11987654321")
        @NotBlank @Size(min = 9, max = 11) String phone,

        @Schema(description = "Matrícula de acesso ao sistema", example = "COORD001")
        @NotBlank String registration,

        @Schema(description = "Senha de acesso (mínimo 8, máximo 60 caracteres)", example = "senha@123")
        @NotBlank @Size(min = 8, max = 60) String password,

        @Schema(description = "Código administrativo do coordenador", example = "1")
        @NotNull Integer codAdmin

) {}