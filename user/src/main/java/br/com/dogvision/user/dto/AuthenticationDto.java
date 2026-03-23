package br.com.dogvision.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para autenticação do usuário")
public record AuthenticationDto(

        @Schema(description = "Matrícula de acesso ao sistema", example = "COORD001")
        String registration,

        @Schema(description = "Senha de acesso", example = "senha@123")
        String password

) {}