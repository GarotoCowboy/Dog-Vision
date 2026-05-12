package br.com.dogvision.user.dto.create;

import br.com.dogvision.user.model.ShiftEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Data for creating a new trainer")
public record CreateTrainerRequest(

        @Schema(description = "Trainer email", example = "trainer@dogvision.com")
        @NotBlank @Email String email,

        @Schema(description = "Trainer full name", example = "Pedro Almeida")
        @NotBlank String name,

        @Schema(description = "Trainer phone (9 to 11 digits)", example = "11987654321")
        @NotBlank @Size(min = 9, max = 11) String phone,

        @Schema(description = "System access registration", example = "TRAIN001")
        @NotBlank String registration,

        @Schema(description = "Access password (minimum 8, maximum 60 characters)", example = "password@123")
        @NotBlank @Size(min = 8, max = 60) String password,

        @Schema(description = "Trainer work shift", example = "MORNING")
        @NotNull ShiftEnum shift,

        @Schema(description = "Trainer area of expertise", example = "Behavioral training")
        @NotBlank String areaOfExpertise

) {}


