package br.com.dogvision.user.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro de um novo usuário")
public record CreateUserRequest(

        @Schema(description = "Matrícula de acesso ao sistema", example = "USER001")
        @NotNull @NotBlank
        String registration,

        @Schema(description = "Senha de acesso (mínimo 8, máximo 12 caracteres)", example = "senha@123")
        @NotNull @NotBlank @Size(min = 8, max = 12)
        String password

) {}