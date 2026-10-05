package br.com.dogvision.dogtraining.model;

import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class Stage1 {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID trainingId;

    @NotNull(message = "Availability is mandatory")
    private boolean availability;

    @NotNull(message = "Sit is mandatory")
    private boolean sit;

    @NotNull(message = "Lie down is mandatory")
    private boolean lieDown;

    @NotNull(message = "Stand up is mandatory")
    private boolean standUp;

    @NotNull(message = "Left is mandatory")
    private boolean left;

    @NotNull(message = "Right is mandatory")
    private boolean right;

    @NotNull(message = "Food control is mandatory")
    private boolean foodControl;

    @NotNull(message = "Box guides is mandatory")
    private boolean boxGuides;

    @NotNull(message = "Leg resistance is mandatory")
    private boolean legResistance;

    @NotNull(message = "Harness resistance is mandatory")
    private boolean harnessResistance;

    @NotNull(message = "Wear harness is mandatory")
    private boolean wearHarness;

    @NotNull(message = "Submission is mandatory")
    private boolean submission;

    @NotNull(message = "Open door is mandatory")
    private boolean openDoor;

    @NotNull(message = "Status is mandatory")
    @Enumerated(EnumType.STRING)
    private TraningStatus status;
}