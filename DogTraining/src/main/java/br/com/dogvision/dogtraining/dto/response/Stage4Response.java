package br.com.dogvision.dogtraining.dto.response;

import br.com.dogvision.dogtraining.model.enums.TraningStatus;

import java.util.UUID;

public record Stage4Response (
        UUID id,
        UUID trainingId,
        boolean wearHarness,
        boolean right,
        boolean left,
        boolean back,
        boolean curb,
        boolean entranceExit,
        boolean stairs,
        boolean sidewalkReturn,
        boolean searchSeat,
        boolean pedestrianCrossing,
        boolean aerialObstacle,
        boolean lateralObstacle,
        boolean groundObstacle,
        int movingObstacle,
        boolean trafficLightCrossing,
        boolean shoppingCenter,
        boolean rolling,
        boolean elevator,
        boolean commercialCenter,
        boolean cityCenter,
        boolean busTerminal,
        boolean bus,
        boolean subway,
        boolean intelligentDisobedience,
        boolean blindfoldedWalk,
        boolean busStop,
        boolean atm,
        boolean bathroomWalk,
        TraningStatus status

){
}
