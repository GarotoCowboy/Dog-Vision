package br.com.dogvision.user.dto.response;

import br.com.dogvision.user.model.EmployeeType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Employee response data")
public record EmployeeResponse(
        @Schema(description = "Employee ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID employeeId,
        @Schema(description = "User ID", example = "550e8400-e29b-41d4-a716-446655440001")
        UUID userId,
        @Schema(description = "System access registration", example = "FUNC001")
        String registration,
        @Schema(description = "Employee email", example = "employee@dogvision.com")
        String email,
        @Schema(description = "Employee full name", example = "Maria Oliveira")
        String name,
        @Schema(description = "Employee phone", example = "11987654321")
        String phone,
        @Schema(description = "Employee work shift", example = "MORNING")
        String shift,
        @Schema(description = "Employee type", example = "VETERINARIAN")
        EmployeeType type
) {}


