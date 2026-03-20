package br.com.dogvision.user.dto.create;

import br.com.dogvision.user.model.EmployeeType;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

public record CreateEmployeeRequest(
        @NotBlank @Email String email,
        @NotBlank String name,
        @NotBlank @CPF @Size(min = 11, max = 11) String cpf,
        @NotBlank @Size(min = 9, max = 11) String phone,

        @NotBlank String registration,
        @NotBlank @Size(min = 8, max = 60) String password,

        @NotNull EmployeeType type
) {}
