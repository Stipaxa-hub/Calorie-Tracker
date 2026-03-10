package com.calorietracker.modules.auth.service;

import com.calorietracker.modules.auth.domain.Gender;
import com.calorietracker.modules.auth.domain.UserProfile;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.stereotype.Service;

@Service
public class CalorieCalculatorService {
    public int calculateDailyCalories(UserProfile userProfile) {
        double bmr = calculateBMR(
                userProfile.getWeightKg(),
                userProfile.getHeighCm(),
                userProfile.getDateOfBirth(),
                userProfile.getGender()
        );

        //Total Daily Energy Expenditure
        double tdee = bmr * userProfile.getActivityLevel().getMultiplier();

        double adjusted = tdee + userProfile.getGoalType().getCalorieAdjustment();

        return (int) Math.round(adjusted);
    }

    //BMR - Basal Metabolic Rate
    public Double calculateBMR(Double weight, Double height,
                               LocalDate dateOfBirth, Gender gender) {
        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();

        double base = (10 * weight) + (6.25 * height) - (5 * age);

        return switch (gender) {
            case MALE -> base + 5;
            case FEMALE -> base - 161;
            default -> base - 78;
        };
    }

    public int calculateProtein(int calories) {
        return (int) Math.round((calories * 0.30) / 4);
    }

    public int calculateCarbs(int calories) {
        return (int) Math.round((calories * 0.40) / 4);
    }

    public int calculateFat(int calories) {
        return (int) Math.round((calories * 0.30) / 9);
    }
}
