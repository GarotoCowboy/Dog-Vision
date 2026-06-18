package br.com.dogvision.user.controller;

import br.com.dogvision.user.dto.response.EmployeeResponse;
import br.com.dogvision.user.dto.create.CreateEmployeeRequest;
import br.com.dogvision.user.dto.update.UpdateEmployeeRequest;
import br.com.dogvision.user.infra.exception.error.ErrorResponse;
import br.com.dogvision.user.service.EmployeeService;
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
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@ApiResponse(
        responseCode = "500",
        description = "Internal server error - Unexpected DogVision failure",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
)
@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
@Tag(name = "Employees", description = "Employee management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    private final EmployeeService service;

    @Operation(summary = "Find employee by ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee found",
                    content = @Content(schema = @Schema(implementation = EmployeeResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getById(
            @Parameter(description = "Employee UUID", required = true)
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Create new employee")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Employee created successfully",
                    content = @Content(schema = @Schema(implementation = EmployeeResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid or missing data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EmployeeResponse> save(@RequestBody @Valid CreateEmployeeRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @Operation(summary = "Update employee data")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee updated successfully",
                    content = @Content(schema = @Schema(implementation = EmployeeResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid or missing data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content),

    })
    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(
            @Parameter(description = "Employee UUID", required = true)
            @PathVariable UUID id,
            @RequestBody UpdateEmployeeRequest dto
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(id, dto));
    }

    @Operation(summary = "List all employees")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List returned successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EmployeeResponse.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content),

    })
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> list() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Delete employee by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Employee deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Employee UUID", required = true)
            @PathVariable UUID id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}


