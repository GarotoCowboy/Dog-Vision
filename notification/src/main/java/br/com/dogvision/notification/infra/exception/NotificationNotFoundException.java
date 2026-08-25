package br.com.dogvision.notification.infra.exception;

import java.util.UUID;
import org.springframework.http.HttpStatus;

public class NotificationNotFoundException extends BusinessException {

    public NotificationNotFoundException(UUID id) {
        super("Notification not found with id: " + id, HttpStatus.NOT_FOUND);
    }

    public NotificationNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

