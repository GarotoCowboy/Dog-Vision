package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.create.CreateCoordinatorRequest;
import br.com.dogvision.user.dto.response.CoordinatorResponse;
import br.com.dogvision.user.infra.exception.error.ErrorResponse;
import br.com.dogvision.user.service.CoordinatorService;
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
@RequestMapping("/api/v1/employees/coordinators")
@Tag(name = "Coordinators", description = "Coordinator management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class CoordinatorController {

    private final CoordinatorService service;

    public CoordinatorController(CoordinatorService service) {
        this.service = service;
    }

    @Operation(summary = "Find coordinator by ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Coordinator found",
                    content = @Content(schema = @Schema(implementation = CoordinatorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Coordinator not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content)
    })
    @GetMapping("/{id}")
    public CoordinatorResponse getById(
            @Parameter(description = "Coordinator UUID", required = true)
            @PathVariable UUID id
    ) {
        return service.getById(id);
    }

    @Operation(summary = "Find coordinator by registration")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Coordinator found",
                    content = @Content(schema = @Schema(implementation = CoordinatorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Coordinator not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content),
    })
    @GetMapping("/registration/{registration}")
    public CoordinatorResponse getByRegistration(
            @Parameter(description = "Coordinator registration", required = true)
            @PathVariable String registration
    ) {
        return service.getByRegistration(registration);
    }

    @Operation(summary = "List all coordinators")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List returned successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CoordinatorResponse.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content),

    })
    @GetMapping
    public List<CoordinatorResponse> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Create new coordinator")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Coordinator created successfully",
                    content = @Content(schema = @Schema(implementation = CoordinatorResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid or missing data", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content),

    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CoordinatorResponse save(@RequestBody @Valid CreateCoordinatorRequest dto) {
        return service.save(dto);
    }

    @Operation(summary = "Delete coordinator by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Coordinator deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Coordinator not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content),

    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "Coordinator UUID", required = true)
            @PathVariable UUID id
    ) {
        service.delete(id);
    }
}

