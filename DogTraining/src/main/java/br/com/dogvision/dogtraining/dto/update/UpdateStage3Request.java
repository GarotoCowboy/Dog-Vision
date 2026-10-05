package br.com.dogvision.dogtraining.dto.update;

import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import java.util.UUID;

public record UpdateStage3Request(
        UUID id,
        UUID trainingId,
        Boolean wearHarness,
        Boolean aerialObstacle,
        Boolean lateralObstacle,
        Boolean back,
        Boolean right,
        Boolean left,
        Boolean groundObstacle,
        Integer movingObstacle,
        Boolean curb,
        Boolean crosswalk,
        Boolean fourCorners,
        Boolean sidewalkReturn,
        Boolean generalObedience,
        Boolean attractionBehavior,
        Boolean recall,
        TraningStatus status
) {}