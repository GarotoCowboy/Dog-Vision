package br.com.dogvision.dogtraining.dto.create;

import br.com.dogvision.dogtraining.model.enums.TraningStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateStage1Request(
        @NotNull(message = "Training ID is required")
        UUID trainingId,

        @NotNull(message = "Availability is mandatory")
        Boolean availability,

        @NotNull(message = "Sit is mandatory")
        Boolean sit,

        @NotNull(message = "Lie down is mandatory")
        Boolean lieDown,

        @NotNull(message = "Stand up is mandatory")
        Boolean standUp,

        @NotNull(message = "Left is mandatory")
        Boolean left,

        @NotNull(message = "Right is mandatory")
        Boolean right,

        @NotNull(message = "Food control is mandatory")
        Boolean foodControl,

        @NotNull(message = "Box guides is mandatory")
        Boolean boxGuides,

        @NotNull(message = "Leg resistance is mandatory")
        Boolean legResistance,

        @NotNull(message = "Harness resistance is mandatory")
        Boolean harnessResistance,

        @NotNull(message = "Wear harness is mandatory")
        Boolean wearHarness,

        @NotNull(message = "Submission is mandatory")
        Boolean submission,

        @NotNull(message = "Open door is mandatory")
        Boolean openDoor,

        @NotNull(message = "Status is mandatory")
        TraningStatus status
) {}