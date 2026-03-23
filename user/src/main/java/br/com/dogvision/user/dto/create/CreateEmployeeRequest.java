package br.com.dogvision.user.dto.create;

import br.com.dogvision.user.model.EmployeeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

@Schema(description = "Dados para cadastro de um novo funcionário")
public record CreateEmployeeRequest(

        @Schema(description = "E-mail do funcionário", example = "funcionario@dogvision.com")
        @NotBlank @Email String email,

        @Schema(description = "Nome completo do funcionário", example = "Maria Oliveira")
        @NotBlank String name,

        @Schema(description = "CPF do funcionário (apenas números, 11 dígitos)", example = "12345678901")
        @NotBlank @CPF @Size(min = 11, max = 11) String cpf,

        @Schema(description = "Telefone do funcionário (9 a 11 dígitos)", example = "11987654321")
        @NotBlank @Size(min = 9, max = 11) String phone,

        @Schema(description = "Matrícula de acesso ao sistema", example = "FUNC001")
        @NotBlank String registration,

        @Schema(description = "Senha de acesso (mínimo 8, máximo 60 caracteres)", example = "senha@123")
        @NotBlank @Size(min = 8, max = 60) String password,

        @Schema(description = "Tipo do funcionário", example = "VETERINARIAN")
        @NotNull EmployeeType type

) {}