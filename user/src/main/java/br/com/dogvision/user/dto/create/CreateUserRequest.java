package br.com.dogvision.user.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotNull
        @NotBlank
        String registration,

        @NotNull
        @NotBlank
        @Size(min = 8, max = 12)
        String password
) {
}
