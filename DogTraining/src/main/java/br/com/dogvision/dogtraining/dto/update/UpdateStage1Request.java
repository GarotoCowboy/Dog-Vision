package br.com.dogvision.dogtraining.dto.update;

import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import java.util.UUID;

public record UpdateStage1Request(
        UUID id,
        UUID trainingId,
        Boolean availability,
        Boolean sit,
        Boolean lieDown,
        Boolean standUp,
        Boolean left,
        Boolean right,
        Boolean foodControl,
        Boolean boxGuides,
        Boolean legResistance,
        Boolean harnessResistance,
        Boolean wearHarness,
        Boolean submission,
        Boolean openDoor,
        TraningStatus status
) {}