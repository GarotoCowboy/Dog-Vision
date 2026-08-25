package br.com.dogvision.notification.model;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.dogvision.notification.model.enums.AudienceType;
import br.com.dogvision.notification.model.enums.MessageType;
import br.com.dogvision.notification.model.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private UUID id;
    private String title;
    private String message;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Enumerated(EnumType.STRING)
    private AudienceType audienceType;

    private UUID createdById;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Boolean isCompleted;

    private UUID completedById;
    private String completedByName;
    private LocalDateTime completedAt;

    private LocalDateTime limitDate;
    
    private LocalDateTime createdAt;

}
