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
public class Stage2 {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID trainingId;

    @NotNull(message = "Hallway availability is mandatory")
    private boolean hallwayAvailability;

    @NotNull(message = "Wear harness is mandatory")
    private boolean wearHarness;

    @NotNull(message = "Left is mandatory")
    private boolean left;

    @NotNull(message = "Right is mandatory")
    private boolean right;

    @NotNull(message = "Forward with harness is mandatory")
    private boolean forwardWithHarness;

    @NotNull(message = "Turning is mandatory")
    private boolean turning;

    @NotNull(message = "Left with harness is mandatory")
    private boolean leftWithHarness;

    @NotNull(message = "Right with harness is mandatory")
    private boolean rightWithHarness;

    @NotNull(message = "Aerial obstacle is mandatory")
    private boolean aerialObstacle;

    @NotNull(message = "Lateral obstacle is mandatory")
    private boolean lateralObstacle;

    @NotNull(message = "Ground obstacle is mandatory")
    private boolean groundObstacle;

    @NotNull(message = "Moving obstacle is mandatory")
    private int movingObstacle;

    @NotNull(message = "Handle varied surfaces is mandatory")
    private boolean variedSurfaces;

    @NotNull(message = "Stop in narrow passages is mandatory")
    private boolean narrowPassages;

    @NotNull(message = "General obedience is mandatory")
    private boolean generalObedience;

    @NotNull(message = "Status is mandatory")
    @Enumerated(EnumType.STRING)
    private TraningStatus status;
}