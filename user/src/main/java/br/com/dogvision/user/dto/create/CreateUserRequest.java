package br.com.dogvision.user.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Data for creating a new user")
public record CreateUserRequest(

        @Schema(description = "System access registration", example = "USER001")
        @NotNull @NotBlank
        String registration,

        @Schema(description = "Access password (minimum 8, maximum 12 characters)", example = "password@123")
        @NotNull @NotBlank @Size(min = 8, max = 12)
        String password

) {}

