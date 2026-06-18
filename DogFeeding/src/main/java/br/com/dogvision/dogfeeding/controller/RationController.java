package br.com.dogvision.dogfeeding.controller;

import br.com.dogvision.dogfeeding.dto.create.CreateRationRequest;
import br.com.dogvision.dogfeeding.dto.response.RationAlertResponse;
import br.com.dogvision.dogfeeding.dto.response.RationResponse;
import br.com.dogvision.dogfeeding.dto.update.UpdateRationRequest;
import br.com.dogvision.dogfeeding.infra.exception.error.ErrorResponse;
import br.com.dogvision.dogfeeding.infra.security.TokenService;
import br.com.dogvision.dogfeeding.model.RationStockStatus;
import br.com.dogvision.dogfeeding.model.RationType;
import br.com.dogvision.dogfeeding.service.RationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@ApiResponse(
        responseCode = "500",
        description = "Internal server error - unexpected DogVision failure",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
)
@RestController
@RequestMapping("/api/v1/dogfeeding/rations")
@AllArgsConstructor
@Tag(name = "Rations", description = "Dog ration stock management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class RationController {

    private final RationService service;
    private final TokenService tokenService;

    @Operation(summary = "Create a new ration")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ration created successfully", content = @Content(schema = @Schema(implementation = RationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @Transactional
    public ResponseEntity<RationResponse> save(@RequestBody @Valid CreateRationRequest dto,
                                               @RequestHeader("Authorization") String authHeader) {
        UUID loggedUserId = extractUserId(authHeader);
        RationResponse response = service.save(dto, loggedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "List all rations")
    @GetMapping
    public ResponseEntity<List<RationResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Find ration by ID")
    @GetMapping("/{id}")
    public ResponseEntity<RationResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Search rations by type or stock status")
    @GetMapping("/search")
    public ResponseEntity<List<RationResponse>> search(@RequestParam(required = false) RationType rationType,
                                                       @RequestParam(required = false) RationStockStatus stockStatus) {
        return ResponseEntity.ok(service.search(rationType, stockStatus));
    }

    @Operation(summary = "List low stock alerts")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alerts listed successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = RationAlertResponse.class))))
    })
    @GetMapping("/alerts")
    public ResponseEntity<List<RationAlertResponse>> alerts() {
        return ResponseEntity.ok(service.alerts());
    }

    @Operation(summary = "Update a ration")
    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<RationResponse> update(@PathVariable UUID id,
                                                 @RequestBody @Valid UpdateRationRequest dto,
                                                 @RequestHeader("Authorization") String authHeader) {
        UUID loggedUserId = extractUserId(authHeader);
        return ResponseEntity.ok(service.update(id, dto, loggedUserId));
    }

    @Operation(summary = "Delete a ration")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable UUID id,
                                       @RequestHeader("Authorization") String authHeader) {
        UUID loggedUserId = extractUserId(authHeader);
        service.delete(id, loggedUserId);
        return ResponseEntity.noContent().build();
    }

    private UUID extractUserId(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return UUID.fromString(tokenService.getIdFromToken(token));
    }
}
