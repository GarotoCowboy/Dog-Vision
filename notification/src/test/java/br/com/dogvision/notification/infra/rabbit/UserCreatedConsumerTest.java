package br.com.dogvision.notification.infra.rabbit;

import br.com.dogvision.notification.dto.events.UserCreatedEvent;
import br.com.dogvision.notification.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserCreatedConsumerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserCreatedConsumer consumer;

    @Test
    void shouldConsumeUserCreatedEventAndSendEmail() {
        UserCreatedEvent event = new UserCreatedEvent(
                "Carlos Souza",
                "carlos@dogvision.com",
                "COL001",
                "TempPass123!"
        );

        consumer.consumeUserCreated(event);

        verify(emailService).sendTemporaryPasswordEmail(
                "carlos@dogvision.com",
                "Carlos Souza",
                "COL001",
                "TempPass123!"
        );
    }
}
