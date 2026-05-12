package br.com.dogvision.user.dto.response;

import br.com.dogvision.user.model.EmployeeType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Coordinator response data")
public record CoordinatorResponse(
        @Schema(description = "Employee ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID employeeId,
        @Schema(description = "User ID", example = "550e8400-e29b-41d4-a716-446655440001")
        UUID userId,
        @Schema(description = "System access registration", example = "COORD001")
        String registration,
        @Schema(description = "Coordinator email", example = "coordinator@dogvision.com")
        String email,
        @Schema(description = "Coordinator full name", example = "John Silva")
        String name,
        @Schema(description = "Coordinator phone", example = "11987654321")
        String phone,
        @Schema(description = "Coordinator work shift", example = "MORNING")
        String shift,
        @Schema(description = "Employee type", example = "COORDINATOR")
        EmployeeType type
) {}


