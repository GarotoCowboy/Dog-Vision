package br.com.dogvision.notification.infra.exception;

import org.springframework.http.HttpStatus;

public class NotificationMessagingException extends BusinessException {

    public NotificationMessagingException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public NotificationMessagingException(String action, Throwable cause) {
        super("Failed to " + action + " message in message broker: " + (cause != null ? cause.getMessage() : "Unknown error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

