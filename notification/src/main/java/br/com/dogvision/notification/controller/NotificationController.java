package br.com.dogvision.notification.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.dogvision.notification.dto.create.NotificationCreateRequest;
import br.com.dogvision.notification.dto.response.NotificationResponse;
import br.com.dogvision.notification.dto.update.NotificationTaskCompletedUpdate;
import br.com.dogvision.notification.infra.exception.error.ErrorResponse;
import br.com.dogvision.notification.infra.security.TokenService;
import br.com.dogvision.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@ApiResponse(
        responseCode = "500",
        description = "Internal server error - unexpected DogVision failure",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
)
@RestController
@RequestMapping("/api/v1/dogNotification/notifications")
@AllArgsConstructor
@Tag(name = "Notifications", description = "Notification and Task Mural management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationService service;
    private final TokenService tokenService;

    @Operation(summary = "Create a new notification")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Notification created successfully", content = @Content(schema = @Schema(implementation = NotificationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PostMapping
    public ResponseEntity<NotificationResponse> create(
            @RequestBody @Valid NotificationCreateRequest dto,
            @RequestHeader("Authorization") String authHeader) {
        UUID loggedUserId = extractUserId(authHeader);
        NotificationResponse response = service.createNotification(dto, loggedUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "List all notifications paginated with optional isCompleted filter, ordered by newest")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notifications listed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> list(
            @RequestParam(required = false) Boolean isCompleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<NotificationResponse> notifications = service.listNotifications(isCompleted, page, size);
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "List only pending (not completed) notifications paginated, ordered by newest")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pending notifications listed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @GetMapping("/pending")
    public ResponseEntity<Page<NotificationResponse>> listPending(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<NotificationResponse> notifications = service.listPendingNotifications(page, size);
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "List only completed notifications paginated, ordered by newest")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Completed notifications listed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @GetMapping("/completed")
    public ResponseEntity<Page<NotificationResponse>> listCompleted(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<NotificationResponse> notifications = service.listCompletedNotifications(page, size);
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "Mark task notification as completed")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task completed status updated successfully", content = @Content(schema = @Schema(implementation = NotificationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PatchMapping("/toggle")
    public ResponseEntity<NotificationResponse> toggleTaskCompleted(
            @RequestBody @Valid NotificationTaskCompletedUpdate dto,
            @RequestHeader("Authorization") String authHeader) {
        UUID loggedUserId = extractUserId(authHeader);
        NotificationResponse response = service.toggleTaskCompleted(dto, loggedUserId);
        return ResponseEntity.ok(response);
    }

    private UUID extractUserId(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return UUID.fromString(tokenService.getIdFromToken(token));
    }
}
