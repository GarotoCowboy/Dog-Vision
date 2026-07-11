package br.com.dogvision.dogfeeding.infra.rabbit.ration;

import br.com.dogvision.dogfeeding.dto.events.RationQuantityUpdatedEvent;
import br.com.dogvision.dogfeeding.model.Ration;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class RationEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishQuantityUpdated(Ration ration){
        RationQuantityUpdatedEvent event = new RationQuantityUpdatedEvent(
                ration.getId(),
                ration.getName(),
                ration.getTotalRationQuantity(),
                ration.getCurrentRationQuantity()
        );

        rabbitTemplate.convertAndSend(
                RationRabbitConfig.RATION_EXCHANGE,
                RationRabbitConfig.RATION_QUANTITY_UPDATED_ROUTING_KEY,
                event
        );
    }
}
