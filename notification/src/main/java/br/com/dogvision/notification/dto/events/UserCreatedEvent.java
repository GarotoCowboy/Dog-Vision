package br.com.dogvision.notification.dto.events;

public record UserCreatedEvent(
        String name,
        String email,
        String registration,
        String temporaryPassword
) {}
