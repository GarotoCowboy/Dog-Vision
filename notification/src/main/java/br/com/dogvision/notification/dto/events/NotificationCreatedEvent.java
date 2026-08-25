package br.com.dogvision.notification.dto.events;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.dogvision.notification.model.enums.MessageType;

public record NotificationCreatedEvent(
    UUID id,
    String title,
    String message,
    MessageType messageType,
    String createdByName,
    LocalDateTime createdAt
) {

}
