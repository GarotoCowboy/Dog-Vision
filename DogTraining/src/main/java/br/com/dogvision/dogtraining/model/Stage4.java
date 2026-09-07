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
public class Stage4 {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID trainingId;

    @NotNull(message = "Wear harness is mandatory")
    private boolean wearHarness;

    @NotNull(message = "Right is mandatory")
    private boolean right;

    @NotNull(message = "Left is mandatory")
    private boolean left;

    @NotNull(message = "Return is mandatory")
    private boolean back;

    @NotNull(message = "Curb is mandatory")
    private boolean curb;

    @NotNull(message = "Entrance and exit is mandatory")
    private boolean entranceExit;

    @NotNull(message = "Stairs is mandatory")
    private boolean stairs;

    @NotNull(message = "Sidewalk return is mandatory")
    private boolean sidewalkReturn;

    @NotNull(message = "Search for seat is mandatory")
    private boolean searchSeat;

    @NotNull(message = "Pedestrian crossing is mandatory")
    private boolean pedestrianCrossing;

    @NotNull(message = "Aerial obstacle is mandatory")
    private boolean aerialObstacle;

    @NotNull(message = "Lateral obstacle is mandatory")
    private boolean lateralObstacle;

    @NotNull(message = "Ground obstacle is mandatory")
    private boolean groundObstacle;

    @NotNull(message = "Moving obstacle is mandatory")
    private int movingObstacle;

    @NotNull(message = "Traffic light crossing is mandatory")
    private boolean trafficLightCrossing;

    @NotNull(message = "Shopping center is mandatory")
    private boolean shoppingCenter;

    @NotNull(message = "Rolling is mandatory")
    private boolean rolling;

    @NotNull(message = "Elevator is mandatory")
    private boolean elevator;

    @NotNull(message = "Commercial center is mandatory")
    private boolean commercialCenter;

    @NotNull(message = "City center is mandatory")
    private boolean cityCenter;

    @NotNull(message = "Bus terminal is mandatory")
    private boolean busTerminal;

    @NotNull(message = "Bus is mandatory")
    private boolean bus;

    @NotNull(message = "Subway is mandatory")
    private boolean subway;

    @NotNull(message = "Intelligent disobedience is mandatory")
    private boolean intelligentDisobedience;

    @NotNull(message = "Blindfolded walk is mandatory")
    private boolean blindfoldedWalk;

    @NotNull(message = "Bus stop is mandatory")
    private boolean busStop;

    @NotNull(message = "ATM is mandatory")
    private boolean atm;

    @NotNull(message = "Bathroom walk is mandatory")
    private boolean bathroomWalk;

    @NotNull(message = "Status is mandatory")
    @Enumerated(EnumType.STRING)
    private TraningStatus status;
}