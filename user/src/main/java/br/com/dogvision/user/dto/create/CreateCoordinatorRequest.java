package br.com.dogvision.user.dto.create;

import jakarta.validation.constraints.*;

public record CreateCoordinatorRequest(

        // Employee
        @NotBlank @Email String email,
        @NotBlank String name,
        @NotBlank @Size(min = 11, max = 11) String cpf,
        @NotBlank @Size(min = 9, max = 11) String phone,

        // User
        @NotBlank String registration,
        @NotBlank @Size(min = 8, max = 60) String password,

        // Specific
        @NotNull Integer codAdmin
) {}
