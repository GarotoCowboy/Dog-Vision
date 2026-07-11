package br.com.dogvision.dogfeeding.infra.rabbit.ration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class RationRabbitConfig {

    public static final String RATION_EXCHANGE = "ration.exchange";
    public static final String RATION_QUANTITY_UPDATED_QUEUE = "ration.quantity.updated.queue";
    public static final String RATION_QUANTITY_UPDATED_ROUTING_KEY =  "ration.quantity.updated";

    @Bean
    public TopicExchange rationExchange(){
        return new TopicExchange(RATION_EXCHANGE);
    }

    @Bean
    public Queue rationQuantityUpdatedQueue(){
        return new Queue(RATION_QUANTITY_UPDATED_QUEUE);
    }

    @Bean
    public Binding rationQuantityUpdatedBinding(){
        return BindingBuilder
                .bind(rationQuantityUpdatedQueue())
                .to(rationExchange())
                .with(RATION_QUANTITY_UPDATED_ROUTING_KEY);
    }


}
