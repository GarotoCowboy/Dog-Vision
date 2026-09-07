package br.com.dogvision.dogtraining.dto.create;

import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateStage2Request(
        @NotNull(message = "Training ID is required")
        UUID trainingId,

        @NotNull(message = "Hallway availability is mandatory")
        Boolean hallwayAvailability,

        @NotNull(message = "Wear harness is mandatory")
        Boolean wearHarness,

        @NotNull(message = "Left is mandatory")
        Boolean left,

        @NotNull(message = "Right is mandatory")
        Boolean right,

        @NotNull(message = "Forward with harness is mandatory")
        Boolean forwardWithHarness,

        @NotNull(message = "Turning is mandatory")
        Boolean turning,

        @NotNull(message = "Left with harness is mandatory")
        Boolean leftWithHarness,

        @NotNull(message = "Right with harness is mandatory")
        Boolean rightWithHarness,

        @NotNull(message = "Aerial obstacle is mandatory")
        Boolean aerialObstacle,

        @NotNull(message = "Lateral obstacle is mandatory")
        Boolean lateralObstacle,

        @NotNull(message = "Ground obstacle is mandatory")
        Boolean groundObstacle,

        @NotNull(message = "Moving obstacle is mandatory")
        Integer movingObstacle,

        @NotNull(message = "Handle varied surfaces is mandatory")
        Boolean variedSurfaces,

        @NotNull(message = "Stop in narrow passages is mandatory")
        Boolean narrowPassages,

        @NotNull(message = "General obedience is mandatory")
        Boolean generalObedience,

        @NotNull(message = "Status is mandatory")
        TraningStatus status
) {}