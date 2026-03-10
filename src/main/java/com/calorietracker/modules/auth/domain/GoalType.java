package com.calorietracker.modules.auth.domain;

import lombok.Getter;

@Getter
public enum GoalType {
    LOSE_WEIGHT(-500),
    MAINTAIN_WEIGHT(0),
    GAIN_WEIGHT(300),
    BUILD_MUSCLE(250),
    IMPROVE_ENDURANCE(0);

    private final int calorieAdjustment;

    GoalType(int calorieAdjustment) {
        this.calorieAdjustment = calorieAdjustment;
    }
}
