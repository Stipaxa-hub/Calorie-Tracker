package com.calorietracker.modules.auth.dto;

import com.calorietracker.modules.auth.domain.ActivityLevel;
import com.calorietracker.modules.auth.domain.Gender;
import com.calorietracker.modules.auth.domain.GoalType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record UserProfileRequestDto(
        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,
        @NotNull(message = "Heigh is required")
        @DecimalMin(value = "50.0", message = "Height must be at least 50cm")
        @DecimalMax(value = "300", message = "Heigh must be under 300cm")
        Double heighCm,
        @NotNull(message = "Weigh is required")
        @DecimalMin(value = "20.0", message = "Weight must be at least 20kg")
        @DecimalMax(value = "500", message = "Weight must  be under 500kg")
        Double weightKg,
        @NotNull(message = "Gender is required")
        Gender gender,
        @NotNull(message = "Activity level is required")
        ActivityLevel activityLevel,
        @NotNull(message = "Goal type is required")
        GoalType goalType
) {
}
