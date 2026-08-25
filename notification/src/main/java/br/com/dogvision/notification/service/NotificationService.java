package br.com.dogvision.notification.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import br.com.dogvision.notification.dto.create.NotificationCreateRequest;
import br.com.dogvision.notification.dto.response.NotificationResponse;
import br.com.dogvision.notification.dto.update.NotificationTaskCompletedUpdate;

public interface NotificationService {

    NotificationResponse createNotification(NotificationCreateRequest notificationCreateRequest,UUID loggedUserId);

    NotificationResponse  toggleTaskCompleted(NotificationTaskCompletedUpdate notificationTaskCompletedUpdate, UUID loggedUserId);
    
    Page<NotificationResponse> listNotifications(int page, int size);
}
