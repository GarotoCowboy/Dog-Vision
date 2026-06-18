package br.com.dogvision.user.dto.update;

import br.com.dogvision.user.model.ShiftEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "Data for updating an employee (all fields are optional)")
public record UpdateEmployeeRequest(

        @Schema(description = "New employee email", example = "novoemail@dogvision.com")
        @Email String email,

        @Schema(description = "New employee full name", example = "Maria Silva Oliveira")
        String name,

        @Schema(description = "New employee phone (9 to 11 digits)", example = "11987654321")
        @Size(min = 9, max = 11) String phone,

        @Schema(description = "New employee work shift", example = "AFTERNOON")
        ShiftEnum shift

) {}

