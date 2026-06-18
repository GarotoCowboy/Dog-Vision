package br.com.dogvision.dogfeeding.controller;

import br.com.dogvision.dogfeeding.dto.create.CreateFeedingRequest;
import br.com.dogvision.dogfeeding.dto.response.ConsumptionReportResponse;
import br.com.dogvision.dogfeeding.dto.response.FeedingResponse;
import br.com.dogvision.dogfeeding.dto.update.UpdateFeedingRequest;
import br.com.dogvision.dogfeeding.infra.exception.error.ErrorResponse;
import br.com.dogvision.dogfeeding.infra.security.TokenService;
import br.com.dogvision.dogfeeding.model.MealType;
import br.com.dogvision.dogfeeding.model.RationType;
import br.com.dogvision.dogfeeding.service.FeedingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@ApiResponse(
        responseCode = "500",
        description = "Internal server error - unexpected DogVision failure",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
)
@RestController
@RequestMapping("/api/v1/dogfeeding/feedings")
@AllArgsConstructor
@Tag(name = "Feedings", description = "Dog feeding management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class FeedingController {

    private final FeedingService service;
    private final TokenService tokenService;

    @Operation(summary = "Create a new feeding")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Feeding created successfully", content = @Content(schema = @Schema(implementation = FeedingResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PostMapping
    @Transactional
    public ResponseEntity<FeedingResponse> save(@RequestBody @Valid CreateFeedingRequest dto,
                                                @RequestHeader("Authorization") String authHeader) {
        UUID loggedUserId = extractUserId(authHeader);
        FeedingResponse response = service.save(dto, loggedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "List all feedings")
    @GetMapping
    public ResponseEntity<List<FeedingResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Find feeding by ID")
    @GetMapping("/{id}")
    public ResponseEntity<FeedingResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "List feedings by dog ID")
    @GetMapping("/dog/{dogId}")
    public ResponseEntity<List<FeedingResponse>> findByDogId(
            @Parameter(description = "Dog UUID", required = true)
            @PathVariable UUID dogId) {
        return ResponseEntity.ok(service.findByDogId(dogId));
    }

    @Operation(summary = "Search feedings by period, dog, meal type or ration type")
    @GetMapping("/search")
    public ResponseEntity<List<FeedingResponse>> search(
            @RequestParam(required = false) UUID dogId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) MealType mealType,
            @RequestParam(required = false) RationType rationType) {
        return ResponseEntity.ok(service.search(dogId, startDate, endDate, mealType, rationType));
    }

    @Operation(summary = "Report dog consumption by ration")
    @GetMapping("/reports/consumption")
    public ResponseEntity<List<ConsumptionReportResponse>> consumptionReport(
            @RequestParam(required = false) UUID dogId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) RationType rationType) {
        return ResponseEntity.ok(service.consumptionReport(dogId, startDate, endDate, rationType));
    }

    @Operation(summary = "Update a feeding")
    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<FeedingResponse> update(@PathVariable UUID id,
                                                  @RequestBody @Valid UpdateFeedingRequest dto,
                                                  @RequestHeader("Authorization") String authHeader) {
        UUID loggedUserId = extractUserId(authHeader);
        return ResponseEntity.ok(service.update(id, dto, loggedUserId));
    }

    @Operation(summary = "Delete a feeding")
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
