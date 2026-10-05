package br.com.dogvision.user.dto.events;

public record UserCreatedEvent(
        String name,
        String email,
        String registration,
        String temporaryPassword
) {}
