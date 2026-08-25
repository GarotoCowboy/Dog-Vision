package br.com.dogvision.notification.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.dogvision.notification.model.enums.AudienceType;
import br.com.dogvision.notification.model.enums.MessageType;
import br.com.dogvision.notification.model.enums.Status;

public record NotificationResponse(
        
     UUID id,
     String title,
     String message,

     MessageType messageType,

     AudienceType audienceType,

     UUID createdById,
     Status status,
     Boolean isCompleted,

     UUID completedById,
     String completedByName,
     LocalDateTime completedAt,

     LocalDateTime limitDate,
     LocalDateTime createdAt
) {
    
}
