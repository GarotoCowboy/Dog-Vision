package br.com.dogvision.dogfeeding.infra.rabbit.ration;

import br.com.dogvision.dogfeeding.dto.events.RationQuantityUpdatedEvent;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RationQuantityConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = RationRabbitConfig.RATION_QUANTITY_UPDATED_QUEUE)
    public void consume(RationQuantityUpdatedEvent event){
        messagingTemplate.convertAndSend("/topic/rations", event);
    }
}
