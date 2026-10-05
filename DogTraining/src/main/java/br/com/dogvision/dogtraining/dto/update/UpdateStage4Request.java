package br.com.dogvision.dogtraining.dto.update;

import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import java.util.UUID;

public record UpdateStage4Request(
        UUID id,
        UUID trainingId,
        Boolean wearHarness,
        Boolean right,
        Boolean left,
        Boolean back,
        Boolean curb,
        Boolean entranceExit,
        Boolean stairs,
        Boolean sidewalkReturn,
        Boolean searchSeat,
        Boolean pedestrianCrossing,
        Boolean aerialObstacle,
        Boolean lateralObstacle,
        Boolean groundObstacle,
        Integer movingObstacle,
        Boolean trafficLightCrossing,
        Boolean shoppingCenter,
        Boolean rolling,
        Boolean elevator,
        Boolean commercialCenter,
        Boolean cityCenter,
        Boolean busTerminal,
        Boolean bus,
        Boolean subway,
        Boolean intelligentDisobedience,
        Boolean blindfoldedWalk,
        Boolean busStop,
        Boolean atm,
        Boolean bathroomWalk,
        TraningStatus status
) {}