package br.com.dogvision.dogtraining.dto.create;

import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateStage3Request(
        @NotNull(message = "Training ID is required")
        UUID trainingId,

        @NotNull(message = "Wear harness is mandatory")
        Boolean wearHarness,

        @NotNull(message = "Aerial obstacle is mandatory")
        Boolean aerialObstacle,

        @NotNull(message = "Lateral obstacle is mandatory")
        Boolean lateralObstacle,

        @NotNull(message = "Return is mandatory")
        Boolean back,

        @NotNull(message = "Right is mandatory")
        Boolean right,

        @NotNull(message = "Left is mandatory")
        Boolean left,

        @NotNull(message = "Ground obstacle is mandatory")
        Boolean groundObstacle,

        @NotNull(message = "Moving obstacle is mandatory")
        Integer movingObstacle,

        @NotNull(message = "Curb is mandatory")
        Boolean curb,

        @NotNull(message = "Crosswalk is mandatory")
        Boolean crosswalk,

        @NotNull(message = "Four corners is mandatory")
        Boolean fourCorners,

        @NotNull(message = "Sidewalk return is mandatory")
        Boolean sidewalkReturn,

        @NotNull(message = "General obedience is mandatory")
        Boolean generalObedience,

        @NotNull(message = "Attraction behavior is mandatory")
        Boolean attractionBehavior,

        @NotNull(message = "Recall is mandatory")
        Boolean recall,

        @NotNull(message = "Status is mandatory")
        TraningStatus status
) {}