package br.com.dogvision.notification.infra.rabbit;

import br.com.dogvision.notification.dto.events.NotificationCreatedEvent;
import br.com.dogvision.notification.dto.events.NotificationTaskCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = NotificationRabbitConfig.NOTIFICATION_CREATED_QUEUE)
    public void consumeNotificationCreated(NotificationCreatedEvent event) {
        log.info("Consuming NotificationCreatedEvent from RabbitMQ: ID=[{}]", event.id());
        messagingTemplate.convertAndSend("/topic/notifications", event);
    }

    @RabbitListener(queues = NotificationRabbitConfig.NOTIFICATION_COMPLETED_QUEUE)
    public void consumeNotificationCompleted(NotificationTaskCompletedEvent event) {
        log.info("Consuming NotificationTaskCompletedEvent from RabbitMQ: ID=[{}]", event.id());
        messagingTemplate.convertAndSend("/topic/tasks-completed", event);
    }
}
