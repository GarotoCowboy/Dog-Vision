package br.com.dogvision.dogtraining.dto.response;

import br.com.dogvision.dogtraining.model.enums.TraningStatus;

import java.util.UUID;

public record Stage2Response (
        UUID id,
        UUID trainingId,
        boolean hallwayAvailability,
        boolean wearHarness,
        boolean left,
        boolean right,
        boolean forwardWithHarness,
        boolean turning,
        boolean leftWithHarness,
        boolean rightWithHarness,
        boolean aerialObstacle,
        boolean lateralObstacle,
        boolean groundObstacle,
        int movingObstacle,
        boolean variedSurfaces,
        boolean narrowPassages,
        boolean generalObedience,
        TraningStatus status
){
}
