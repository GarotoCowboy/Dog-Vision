package br.com.dogvision.notification.dto.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationTaskCompletedEvent(
     UUID id,
     UUID completedById,
     String completedByName,
     LocalDateTime completedAt
) {

}
