package br.com.dogvision.user.dto.response;

import br.com.dogvision.user.model.EmployeeType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Collaborator response data")
public record CollaboratorResponse(
        @Schema(description = "Collaborator ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID collaboratorId,
        @Schema(description = "User ID", example = "550e8400-e29b-41d4-a716-446655440001")
        UUID userId,
        @Schema(description = "System access registration", example = "MON001")
        String registration,
        @Schema(description = "Collaborator email", example = "collaborator@dogvision.com")
        String email,
        @Schema(description = "Collaborator full name", example = "Carlos Souza")
        String name,
        @Schema(description = "Collaborator phone", example = "11987654321")
        String phone,
        @Schema(description = "Employee type", example = "COLLABORATOR")
        EmployeeType type,
        @Schema(description = "Collaborator work shift", example = "MORNING")
        String shift
) {}

