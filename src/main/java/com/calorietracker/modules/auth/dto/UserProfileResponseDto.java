package com.calorietracker.modules.auth.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserProfileResponseDto(
        Long id,
        String fullName,
        String email,
        LocalDate dateOfBirth,
        Integer age,
        Double heighCm,
        Double weighKg,
        String gender,
        String activityLevel,
        String goalType,
        Integer dailyCalorieGoal,
        Integer dailyProteinGoal,
        Integer dailyCarbGoal,
        Integer dailyFatGoal,
        Boolean profileComplete,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
