package br.com.dogvision.user.dto.response;

import br.com.dogvision.user.model.EmployeeType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Trainer response data")
public record TrainerResponse(
        @Schema(description = "Employee ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID employeeId,
        @Schema(description = "User ID", example = "550e8400-e29b-41d4-a716-446655440001")
        UUID userId,
        @Schema(description = "System access registration", example = "TRAIN001")
        String registration,
        @Schema(description = "Trainer email", example = "trainer@dogvision.com")
        String email,
        @Schema(description = "Trainer full name", example = "Pedro Almeida")
        String name,
        @Schema(description = "Trainer phone", example = "11987654321")
        String phone,
        @Schema(description = "Trainer work shift", example = "MORNING")
        String shift,
        @Schema(description = "Employee type", example = "TRAINER")
        EmployeeType type,
        @Schema(description = "Trainer area of expertise", example = "Behavioral training")
        String areaOfExpertise
) {}


