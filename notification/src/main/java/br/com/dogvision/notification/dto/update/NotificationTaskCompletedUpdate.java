package br.com.dogvision.notification.dto.update;

import java.rmi.server.UID;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.dogvision.notification.model.enums.AudienceType;
import br.com.dogvision.notification.model.enums.MessageType;

public record NotificationTaskCompletedUpdate(
     UUID id,
     UUID completedById,
     String completedByName,
     LocalDateTime completedAt

) {

}