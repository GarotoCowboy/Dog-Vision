package br.com.dogvision.user.dto.create;

import br.com.dogvision.user.model.ShiftEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Data for creating a new collaborator")
public record CreateCollaboratorRequest(

        @Schema(description = "Collaborator email", example = "collaborator@dogvision.com")
        @NotBlank @Email String email,

        @Schema(description = "Collaborator full name", example = "Carlos Souza")
        @NotBlank String name,

        @Schema(description = "Collaborator phone (9 to 11 digits)", example = "11987654321")
        @NotBlank @Size(min = 9, max = 11) String phone,

        @Schema(description = "System access registration", example = "MON001")
        @NotBlank String registration,

        @Schema(description = "Access password (minimum 8, maximum 60 characters)", example = "password@123")
        @NotBlank @Size(min = 8, max = 60) String password,

        @Schema(description = "Collaborator work shift", example = "MORNING")
        @NotNull ShiftEnum shift

) {}


