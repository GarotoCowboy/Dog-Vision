package br.com.dogvision.notification.dto.update;

import java.util.UUID;

import br.com.dogvision.notification.model.enums.Status;

public record NotificationStatusUpdate (
    UUID id,
    Status status
){
    
}
