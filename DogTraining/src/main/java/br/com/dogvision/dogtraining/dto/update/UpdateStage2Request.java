package br.com.dogvision.dogtraining.dto.update;

import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import java.util.UUID;

public record UpdateStage2Request(
        UUID id,
        UUID trainingId,
        Boolean hallwayAvailability,
        Boolean wearHarness,
        Boolean left,
        Boolean right,
        Boolean forwardWithHarness,
        Boolean turning,
        Boolean leftWithHarness,
        Boolean rightWithHarness,
        Boolean aerialObstacle,
        Boolean lateralObstacle,
        Boolean groundObstacle,
        Integer movingObstacle,
        Boolean variedSurfaces,
        Boolean narrowPassages,
        Boolean generalObedience,
        TraningStatus status
) {}