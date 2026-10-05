package br.com.dogvision.dogtraining.dto.response;

import br.com.dogvision.dogtraining.model.enums.TraningStatus;

import java.util.UUID;

public record Stage1Response(
        UUID id,
        UUID trainingId,
        boolean availability,
        boolean sit,
        boolean lieDown,
        boolean standUp,
        boolean left,
        boolean right,
        boolean foodControl,
        boolean boxGuides,
        boolean legResistance,
        boolean harnessResistance,
        boolean wearHarness,
        boolean submission,
        boolean openDoor,
        TraningStatus status
) {
}
