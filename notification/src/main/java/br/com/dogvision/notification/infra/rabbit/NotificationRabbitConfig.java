package br.com.dogvision.notification.infra.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationRabbitConfig {

    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";


    public static final String NOTIFICATION_CREATED_QUEUE = "notification.created.queue";
    public static final String NOTIFICATION_COMPLETED_QUEUE = "notification.completed.queue";


    public static final String NOTIFICATION_CREATED_ROUTING_KEY = "notification.created";
    public static final String NOTIFICATION_COMPLETED_ROUTING_KEY = "notification.completed";


    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Queue notificationCreatedQueue() {
        return new Queue(NOTIFICATION_CREATED_QUEUE,true);
    }

    @Bean
    public Queue notificationCompletedQueue() {
        return new Queue(NOTIFICATION_COMPLETED_QUEUE,true);
    }

    @Bean
    public Binding notificationCreatedBinding(){
        return BindingBuilder
        .bind(notificationCreatedQueue())
        .to(notificationExchange())
        .with(NOTIFICATION_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding notificationCompletedBinding(){
        return BindingBuilder
        .bind(notificationCompletedQueue())
        .to(notificationExchange())
        .with(NOTIFICATION_COMPLETED_ROUTING_KEY);
    }
}
