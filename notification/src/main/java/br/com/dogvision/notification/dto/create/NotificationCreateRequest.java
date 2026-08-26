package br.com.dogvision.notification.dto.create;

import java.time.LocalDateTime;

import br.com.dogvision.notification.model.enums.AudienceType;
import br.com.dogvision.notification.model.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationCreateRequest(
    @NotBlank(message = "Title is required")
    String title,

    @NotBlank(message = "Message is required")
    String message,

    @NotNull(message = "Message type is required")
    MessageType messageType,

    @NotNull(message = "Audience type is required")
    AudienceType audienceType,

    LocalDateTime eventDate,

    LocalDateTime limitDate
) {
}