package br.com.dogvision.user.dto.response;

import br.com.dogvision.user.model.EmployeeType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Veterinarian response data")
public record VeterinarianResponse(
        @Schema(description = "Employee ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID employeeId,
        @Schema(description = "User ID", example = "550e8400-e29b-41d4-a716-446655440001")
        UUID userId,
        @Schema(description = "System access registration", example = "VET001")
        String registration,
        @Schema(description = "Veterinarian email", example = "veterinarian@dogvision.com")
        String email,
        @Schema(description = "Veterinarian full name", example = "Anna Costa")
        String name,
        @Schema(description = "Veterinarian phone", example = "11987654321")
        String phone,
        @Schema(description = "Veterinarian work shift", example = "MORNING")
        String shift,
        @Schema(description = "Employee type", example = "VETERINARIAN")
        EmployeeType type,
        @Schema(description = "Veterinarian CRMV number", example = "SP-12345")
        String crmv,
        @Schema(description = "Veterinarian area of expertise", example = "General practice")
        String areaOfExpertise
) {}


