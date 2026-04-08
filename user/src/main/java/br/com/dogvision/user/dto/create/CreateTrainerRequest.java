package br.com.dogvision.user.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

@Schema(description = "Dados para cadastro de um novo treinador")
public record CreateTrainerRequest(

        @Schema(description = "E-mail do treinador", example = "treinador@dogvision.com")
        @NotBlank @Email String email,

        @Schema(description = "Nome completo do treinador", example = "Pedro Almeida")
        @NotBlank String name,

        @CPF
        @Schema(description = "CPF do treinador (apenas números, 11 dígitos)", example = "12345678901")
        @NotBlank @Size(min = 11, max = 11) String cpf,

        @Schema(description = "Telefone do treinador (9 a 11 dígitos)", example = "11987654321")
        @NotBlank @Size(min = 9, max = 11) String phone,

        @Schema(description = "Matrícula de acesso ao sistema", example = "TRAIN001")
        @NotBlank String registration,

        @Schema(description = "Senha de acesso (mínimo 8, máximo 60 caracteres)", example = "senha@123")
        @NotBlank @Size(min = 8, max = 60) String password,

        @Schema(description = "Área de especialização do treinador", example = "Adestramento comportamental")
        @NotBlank String areaOfExpertise

) {}