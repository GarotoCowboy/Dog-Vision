package br.com.dogvision.notification.infra.rabbit;

import br.com.dogvision.notification.dto.events.UserCreatedEvent;
import br.com.dogvision.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCreatedConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = NotificationRabbitConfig.USER_CREATED_NOTIFICATION_QUEUE)
    public void consumeUserCreated(UserCreatedEvent event) {
        log.info("Received UserCreatedEvent from RabbitMQ: email=[{}], registration=[{}]",
                event.email(), event.registration());
        try {
            emailService.sendTemporaryPasswordEmail(
                    event.email(),
                    event.name(),
                    event.registration(),
                    event.temporaryPassword()
            );
        } catch (Exception e) {
            log.error("Failed to process UserCreatedEvent for email [{}]: {}", event.email(), e.getMessage(), e);
            throw e;
        }
    }
}
