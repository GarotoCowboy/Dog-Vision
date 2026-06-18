package br.com.dogvision.user.dto.create;

import br.com.dogvision.user.model.ShiftEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Data for creating a new veterinarian")
public record CreateVeterinarianRequest(

        @Schema(description = "Veterinarian email", example = "veterinarian@dogvision.com")
        @NotBlank @Email String email,

        @Schema(description = "Veterinarian full name", example = "Anna Costa")
        @NotBlank String name,

        @Schema(description = "Veterinarian phone (9 to 11 digits)", example = "11987654321")
        @NotBlank @Size(min = 9, max = 11) String phone,

        @Schema(description = "System access registration", example = "VET001")
        @NotBlank String registration,

        @Schema(description = "Access password (minimum 8, maximum 60 characters)", example = "password@123")
        @NotBlank @Size(min = 8, max = 60) String password,

        @Schema(description = "Veterinarian work shift", example = "MORNING")
        @NotNull ShiftEnum shift,

        @Schema(description = "Veterinarian CRMV number", example = "SP-12345")
        @NotBlank String crmv,

        @Schema(description = "Veterinarian area of expertise", example = "General practice")
        @NotBlank String areaOfExpertise

) {}


