package br.com.dogvision.notification.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.dogvision.notification.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    
}
