package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.create.CreateCollaboratorRequest;
import br.com.dogvision.user.dto.response.CollaboratorResponse;
import br.com.dogvision.user.infra.exception.error.ErrorResponse;
import br.com.dogvision.user.service.CollaboratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@ApiResponse(
        responseCode = "500",
        description = "Internal server error - Unexpected DogVision failure",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
)
@RestController
@RequestMapping("/api/v1/employees/collaborators")
@Tag(name = "Collaborators", description = "Collaborator management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class CollaboratorController {

    private final CollaboratorService service;

    public CollaboratorController(CollaboratorService service) {
        this.service = service;
    }

    @Operation(summary = "Find collaborator by ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Collaborator found",
                    content = @Content(schema = @Schema(implementation = CollaboratorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Collaborator not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content)
    })
    @GetMapping("/{id}")
    public CollaboratorResponse getById(
            @Parameter(description = "Collaborator UUID", required = true)
            @PathVariable UUID id
    ) {
        return service.getById(id);
    }

    @Operation(summary = "Find collaborator by registration")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Collaborator found",
                    content = @Content(schema = @Schema(implementation = CollaboratorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Collaborator not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content),

    })
    @GetMapping("/registration/{registration}")
    public CollaboratorResponse getByRegistration(
            @Parameter(description = "Collaborator registration", required = true)
            @PathVariable String registration
    ) {
        return service.getByRegistration(registration);
    }

    @Operation(summary = "List all collaborators")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List returned successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CollaboratorResponse.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content),

    })
    @GetMapping
    public List<CollaboratorResponse> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Create new collaborator")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Collaborator created successfully",
                    content = @Content(schema = @Schema(implementation = CollaboratorResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid or missing data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content),

    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CollaboratorResponse save(@RequestBody @Valid CreateCollaboratorRequest dto) {
        return service.save(dto);
    }

    @Operation(summary = "Delete collaborator by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Collaborator deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Collaborator not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content),
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "Collaborator UUID", required = true)
            @PathVariable UUID id
    ) {
        service.delete(id);
    }
}

