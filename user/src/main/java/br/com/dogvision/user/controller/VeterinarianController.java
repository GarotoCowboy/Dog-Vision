package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.create.CreateVeterinarianRequest;
import br.com.dogvision.user.dto.response.VeterinarianResponse;
import br.com.dogvision.user.infra.exception.error.ErrorResponse;
import br.com.dogvision.user.service.VeterinarianService;
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
@RequestMapping("/api/v1/employees/veterinarians")
@Tag(name = "Veterinarians", description = "Veterinarian management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class VeterinarianController {

    private final VeterinarianService service;

    public VeterinarianController(VeterinarianService service) {
        this.service = service;
    }

    @Operation(summary = "Find veterinarian by ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Veterinarian found",
                    content = @Content(schema = @Schema(implementation = VeterinarianResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Veterinarian not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content)
    })
    @GetMapping("/{id}")
    public VeterinarianResponse getById(
            @Parameter(description = "Veterinarian UUID", required = true)
            @PathVariable UUID id
    ) {
        return service.getById(id);
    }

    @Operation(summary = "Find veterinarian by registration")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Veterinarian found",
                    content = @Content(schema = @Schema(implementation = VeterinarianResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Veterinarian not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content)
    })
    @GetMapping("/registration/{registration}")
    public VeterinarianResponse getByRegistration(
            @Parameter(description = "Veterinarian registration", required = true)
            @PathVariable String registration
    ) {
        return service.getByRegistration(registration);
    }

    @Operation(summary = "List all veterinarians")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List returned successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = VeterinarianResponse.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content)
    })
    @GetMapping
    public List<VeterinarianResponse> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Create new veterinarian")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Veterinarian created successfully",
                    content = @Content(schema = @Schema(implementation = VeterinarianResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid or missing data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VeterinarianResponse save(@RequestBody @Valid CreateVeterinarianRequest dto) {
        return service.save(dto);
    }

    @Operation(summary = "Delete veterinarian by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Veterinarian deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Veterinarian not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "Veterinarian UUID", required = true)
            @PathVariable UUID id
    ) {
        service.delete(id);
    }
}

