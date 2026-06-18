package br.com.dogvision.user.dto.create;

import br.com.dogvision.user.model.ShiftEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Data for creating a new coordinator")
public record CreateCoordinatorRequest(

        @Schema(description = "Coordinator email", example = "coordinator@dogvision.com")
        @NotBlank @Email String email,

        @Schema(description = "Coordinator full name", example = "John Silva")
        @NotBlank String name,

        @Schema(description = "Coordinator phone (9 to 11 digits)", example = "11987654321")
        @NotBlank @Size(min = 9, max = 11) String phone,

        @Schema(description = "System access registration", example = "COORD001")
        @NotBlank String registration,

        @Schema(description = "Access password (minimum 8, maximum 60 characters)", example = "password@123")
        @NotBlank @Size(min = 8, max = 60) String password,

        @Schema(description = "Coordinator work shift", example = "MORNING")
        @NotNull ShiftEnum shift

) {}


