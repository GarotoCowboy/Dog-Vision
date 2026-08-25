package br.com.dogvision.notification.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.dogvision.notification.dto.create.NotificationCreateRequest;
import br.com.dogvision.notification.dto.events.NotificationCreatedEvent;
import br.com.dogvision.notification.dto.events.NotificationTaskCompletedEvent;
import br.com.dogvision.notification.dto.response.NotificationResponse;
import br.com.dogvision.notification.model.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isCompleted", ignore = true)
    @Mapping(target = "createdById", ignore = true)
    @Mapping(target = "completedById", ignore = true)
    @Mapping(target = "completedByName", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Notification toEntity(NotificationCreateRequest dto);

    NotificationResponse toResponse(Notification entity);

    List<NotificationResponse> toResponseList(List<Notification> entities);

    //RABBITMQ events
    NotificationCreatedEvent toCreatedEvent(Notification entity);
    NotificationTaskCompletedEvent toTaskCompletedEvent(Notification entity);
}

