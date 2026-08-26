package br.com.dogvision.notification.dto.events;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.dogvision.notification.model.enums.AudienceType;
import br.com.dogvision.notification.model.enums.MessageType;
import br.com.dogvision.notification.model.enums.Status;

public record NotificationCreatedEvent(
    UUID id,
    String title,
    String message,
    MessageType messageType,
    AudienceType audienceType,
    UUID createdById,
    String createdByName,
    Status status,
    Boolean isCompleted,
    LocalDateTime eventDate,
    LocalDateTime limitDate,
    LocalDateTime createdAt
) {

}
