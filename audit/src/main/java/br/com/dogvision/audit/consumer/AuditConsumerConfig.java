package br.com.dogvision.audit.consumer;

import br.com.dogvision.audit.dto.AuditEventMessage;
import br.com.dogvision.audit.model.AuditEvent;
import br.com.dogvision.audit.repository.AuditoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class AuditConsumerConfig {

    @Bean
    public Consumer<AuditEventMessage> auditIn(AuditoryRepository repository) {
        return message -> {
            AuditEvent event = new AuditEvent();
            event.setRequestId(message.requestId());
            event.setUserRegistration(message.userRegistration());
            event.setGatewayName(message.gatewayName());
            event.setServiceName(message.serviceName());
            event.setMethod(message.method());
            event.setPath(message.path());
            event.setAction(message.action());
            event.setStatusCode(message.statusCode());
            event.setSuccess(message.success());
            event.setIp(message.ip());
            event.setErrorMessage(message.errorMessage());
            event.setLatencyMs(message.latencyMs());
            event.setCreatedAt(message.createdAt());

            repository.save(event);
        };
    }
}
