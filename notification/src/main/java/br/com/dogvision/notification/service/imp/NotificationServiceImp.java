package br.com.dogvision.notification.service.imp;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.dogvision.notification.dto.create.NotificationCreateRequest;
import br.com.dogvision.notification.dto.events.NotificationCreatedEvent;
import br.com.dogvision.notification.dto.events.NotificationTaskCompletedEvent;
import br.com.dogvision.notification.dto.mapper.NotificationMapper;
import br.com.dogvision.notification.dto.response.NotificationResponse;
import br.com.dogvision.notification.dto.update.NotificationTaskCompletedUpdate;
import br.com.dogvision.notification.infra.exception.NotificationMessagingException;
import br.com.dogvision.notification.infra.exception.NotificationNotFoundException;
import br.com.dogvision.notification.infra.rabbit.NotificationRabbitConfig;
import br.com.dogvision.notification.model.Notification;
import br.com.dogvision.notification.model.enums.Status;
import br.com.dogvision.notification.repository.NotificationRepository;
import br.com.dogvision.notification.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationServiceImp implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper mapper;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public NotificationResponse createNotification(NotificationCreateRequest notificationCreateRequest,
            UUID loggedUserId) {

        Notification notification = mapper.toEntity(notificationCreateRequest);
        notification.setCreatedById(loggedUserId);
        notification.setStatus(Status.WAITING);
        notification.setIsCompleted(false);
        notification.setCreatedAt(LocalDateTime.now());

        Notification savedNotification = notificationRepository.save(notification);
        log.info("try to Sending message to RabbitMQ...");
        try {

            NotificationCreatedEvent event = mapper.toCreatedEvent(savedNotification);

            rabbitTemplate.convertAndSend(NotificationRabbitConfig.NOTIFICATION_EXCHANGE,
                    NotificationRabbitConfig.NOTIFICATION_CREATED_ROUTING_KEY,
                    event);
            savedNotification.setStatus(Status.SENT);
            notificationRepository.save(savedNotification);

            log.info("Message sent successfully to RabbitMQ! ID: [{}], Exchange: [{}], RoutingKey: [{}]",
                    savedNotification.getId(),
                    NotificationRabbitConfig.NOTIFICATION_EXCHANGE,
                    NotificationRabbitConfig.NOTIFICATION_CREATED_ROUTING_KEY);

        } catch (Exception e) {
            log.error("Error to send message to RabbitMQ: {}", e.getMessage());
            savedNotification.setStatus(Status.FAILED);
            notificationRepository.save(savedNotification);
        }

        return mapper.toResponse(savedNotification);

    }

    @Override
    public Page<NotificationResponse> listNotifications(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<NotificationResponse> notificationPage = notificationRepository.findAll(pageable)
                .map(mapper::toResponse);

        return notificationPage;
    }

    @Override
    @Transactional
    public NotificationResponse toggleTaskCompleted(NotificationTaskCompletedUpdate notificationTaskCompletedUpdate,
            UUID loggedUserId) {
        
                Notification notification = notificationRepository.findById(notificationTaskCompletedUpdate.id())
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with ID: " + notificationTaskCompletedUpdate.id()));


                if(Boolean.TRUE.equals(notification.getIsCompleted())){
                    return mapper.toResponse(notification);
                }

                notification.setIsCompleted(true);
                notification.setCompletedById(loggedUserId);
                notification.setCompletedByName(notificationTaskCompletedUpdate.completedByName());
                notification.setCompletedAt(LocalDateTime.now());

                Notification savedNotification = notificationRepository.save(notification);
                NotificationTaskCompletedEvent event = mapper.toTaskCompletedEvent(savedNotification);   

                try{
                    rabbitTemplate.convertAndSend(NotificationRabbitConfig.NOTIFICATION_EXCHANGE,
                    NotificationRabbitConfig.NOTIFICATION_COMPLETED_ROUTING_KEY,
                    event);

                    log.info("Task completed successfully and event sent to RabbitMQ! Notification ID: [{}]",
                    savedNotification.getId());
                }catch(Exception e){
                    log.error("Error to send task completed message to RabbitMQ: {}", e.getMessage(), e);                    
                    throw new NotificationMessagingException("Error to publish message to RabbitMQ: " + e.getMessage(),e);
                }

        return mapper.toResponse(savedNotification);
    }

}
