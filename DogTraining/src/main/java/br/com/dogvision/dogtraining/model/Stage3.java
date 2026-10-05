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
public class Stage3 {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID trainingId;

    @NotNull(message = "Wear harness is mandatory")
    private boolean wearHarness;

    @NotNull(message = "Aerial obstacle is mandatory")
    private boolean aerialObstacle;

    @NotNull(message = "Lateral obstacle is mandatory")
    private boolean lateralObstacle;

    @NotNull(message = "Return is mandatory")
    private boolean back;

    @NotNull(message = "Right is mandatory")
    private boolean right;

    @NotNull(message = "Left is mandatory")
    private boolean left;

    @NotNull(message = "Ground obstacle is mandatory")
    private boolean groundObstacle;

    @NotNull(message = "Moving obstacle is mandatory")
    private int movingObstacle;

    @NotNull(message = "Curb is mandatory")
    private boolean curb;

    @NotNull(message = "Crosswalk is mandatory")
    private boolean crosswalk;

    @NotNull(message = "Four corners is mandatory")
    private boolean fourCorners;

    @NotNull(message = "Sidewalk return is mandatory")
    private boolean sidewalkReturn;

    @NotNull(message = "General obedience is mandatory")
    private boolean generalObedience;

    @NotNull(message = "Attraction behavior is mandatory")
    private boolean attractionBehavior;

    @NotNull(message = "Recall is mandatory")
    private boolean recall;

    @NotNull(message = "Status is mandatory")
    @Enumerated(EnumType.STRING)
    private TraningStatus status;
}