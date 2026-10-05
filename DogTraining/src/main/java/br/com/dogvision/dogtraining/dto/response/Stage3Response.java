package br.com.dogvision.dogtraining.dto.response;

import br.com.dogvision.dogtraining.model.enums.TraningStatus;

import java.util.UUID;

public record Stage3Response (
        UUID id,
        UUID trainingId,
        boolean wearHarness,
        boolean aerialObstacle,
        boolean lateralObstacle,
        boolean back,
        boolean right,
        boolean left,
        boolean groundObstacle,
        int movingObstacle,
        boolean curb,
        boolean crosswalk,
        boolean fourCorners,
        boolean sidewalkReturn,
        boolean generalObedience,
        boolean attractionBehavior,
        boolean recall,
        TraningStatus status
){
}
