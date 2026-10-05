package br.com.dogvision.dogtraining.dto.create;

import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateStage4Request(
        @NotNull(message = "Training ID is required")
        UUID trainingId,

        @NotNull(message = "Wear harness is mandatory")
        Boolean wearHarness,

        @NotNull(message = "Right is mandatory")
        Boolean right,

        @NotNull(message = "Left is mandatory")
        Boolean left,

        @NotNull(message = "Return is mandatory")
        Boolean back,

        @NotNull(message = "Curb is mandatory")
        Boolean curb,

        @NotNull(message = "Entrance and exit is mandatory")
        Boolean entranceExit,

        @NotNull(message = "Stairs is mandatory")
        Boolean stairs,

        @NotNull(message = "Sidewalk return is mandatory")
        Boolean sidewalkReturn,

        @NotNull(message = "Search for seat is mandatory")
        Boolean searchSeat,

        @NotNull(message = "Pedestrian crossing is mandatory")
        Boolean pedestrianCrossing,

        @NotNull(message = "Aerial obstacle is mandatory")
        Boolean aerialObstacle,

        @NotNull(message = "Lateral obstacle is mandatory")
        Boolean lateralObstacle,

        @NotNull(message = "Ground obstacle is mandatory")
        Boolean groundObstacle,

        @NotNull(message = "Moving obstacle is mandatory")
        Integer movingObstacle,

        @NotNull(message = "Traffic light crossing is mandatory")
        Boolean trafficLightCrossing,

        @NotNull(message = "Shopping center is mandatory")
        Boolean shoppingCenter,

        @NotNull(message = "Rolling is mandatory")
        Boolean rolling,

        @NotNull(message = "Elevator is mandatory")
        Boolean elevator,

        @NotNull(message = "Commercial center is mandatory")
        Boolean commercialCenter,

        @NotNull(message = "City center is mandatory")
        Boolean cityCenter,

        @NotNull(message = "Bus terminal is mandatory")
        Boolean busTerminal,

        @NotNull(message = "Bus is mandatory")
        Boolean bus,

        @NotNull(message = "Subway is mandatory")
        Boolean subway,

        @NotNull(message = "Intelligent disobedience is mandatory")
        Boolean intelligentDisobedience,

        @NotNull(message = "Blindfolded walk is mandatory")
        Boolean blindfoldedWalk,

        @NotNull(message = "Bus stop is mandatory")
        Boolean busStop,

        @NotNull(message = "ATM is mandatory")
        Boolean atm,

        @NotNull(message = "Bathroom walk is mandatory")
        Boolean bathroomWalk,

        @NotNull(message = "Status is mandatory")
        TraningStatus status
) {}