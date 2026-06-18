package br.com.dogvision.dogfeeding.controller;

import br.com.dogvision.dogfeeding.dto.create.CreateFeedingPlanRequest;
import br.com.dogvision.dogfeeding.dto.response.FeedingPlanResponse;
import br.com.dogvision.dogfeeding.dto.update.UpdateFeedingPlanRequest;
import br.com.dogvision.dogfeeding.infra.exception.error.ErrorResponse;
import br.com.dogvision.dogfeeding.infra.security.TokenService;
import br.com.dogvision.dogfeeding.service.FeedingPlanService;
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
@RequestMapping("/api/v1/dogfeeding/plans")
@AllArgsConstructor
@Tag(name = "Feeding Plans", description = "Dog feeding plan management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class FeedingPlanController {

    private final FeedingPlanService service;
    private final TokenService tokenService;

    @Operation(summary = "Create a new feeding plan")
    @PostMapping
    @Transactional
    public ResponseEntity<FeedingPlanResponse> save(@RequestBody @Valid CreateFeedingPlanRequest dto,
                                                    @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto, extractUserId(authHeader)));
    }

    @Operation(summary = "List all feeding plans")
    @GetMapping
    public ResponseEntity<List<FeedingPlanResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Find feeding plan by ID")
    @GetMapping("/{id}")
    public ResponseEntity<FeedingPlanResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "List feeding plans by dog ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plans listed successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = FeedingPlanResponse.class))))
    })
    @GetMapping("/dog/{dogId}")
    public ResponseEntity<List<FeedingPlanResponse>> findByDogId(@PathVariable UUID dogId) {
        return ResponseEntity.ok(service.findByDogId(dogId));
    }

    @Operation(summary = "Update a feeding plan")
    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<FeedingPlanResponse> update(@PathVariable UUID id,
                                                      @RequestBody @Valid UpdateFeedingPlanRequest dto,
                                                      @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(service.update(id, dto, extractUserId(authHeader)));
    }

    @Operation(summary = "Delete a feeding plan")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable UUID id,
                                       @RequestHeader("Authorization") String authHeader) {
        service.delete(id, extractUserId(authHeader));
        return ResponseEntity.noContent().build();
    }

    private UUID extractUserId(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return UUID.fromString(tokenService.getIdFromToken(token));
    }
}
