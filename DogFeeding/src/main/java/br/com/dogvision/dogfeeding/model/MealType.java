package br.com.dogvision.dogfeeding.model;

import java.time.LocalTime;

public enum MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    POST_TRAINING,
    SNACK;

    public LocalTime defaultTime() {
        return switch (this) {
            case BREAKFAST -> LocalTime.of(8, 0);
            case LUNCH -> LocalTime.of(12, 0);
            case DINNER -> LocalTime.of(18, 0);
            case POST_TRAINING -> LocalTime.of(20, 0);
            case SNACK -> LocalTime.of(15, 0);
        };
    }
}
