package com.calorietracker.modules.auth.service;

import com.calorietracker.common.exception.ResourceNotFoundException;
import com.calorietracker.modules.auth.domain.User;
import com.calorietracker.modules.auth.domain.UserProfile;
import com.calorietracker.modules.auth.dto.UserProfileRequestDto;
import com.calorietracker.modules.auth.dto.UserProfileResponseDto;
import com.calorietracker.modules.auth.mapper.UserProfileMapper;
import com.calorietracker.modules.auth.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final CalorieCalculatorService calorieCalculatorService;

    public void createEmptyProfile(User user) {
        UserProfile profile = UserProfile.builder()
                .user(user)
                .profileComplete(false)
                .build();

        userProfileRepository.save(profile);
        log.info("Empty profile created for user: {}", user.getEmail());
    }

    public UserProfileResponseDto setupProfile(Long userId,
                                               UserProfileRequestDto userProfileRequestDto) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", userId));

        userProfileMapper.updateEntity(userProfileRequestDto, userProfile);
        userProfile.setProfileComplete(true);

        int calories = calorieCalculatorService.calculateDailyCalories(userProfile);
        int proteins = calorieCalculatorService.calculateProtein(calories);
        int carbs = calorieCalculatorService.calculateCarbs(calories);
        int fats = calorieCalculatorService.calculateFat(calories);

        userProfile.setDailyCalorieGoal(calories);
        userProfile.setDailyProteinGoal(proteins);
        userProfile.setDailyCarbGoal(carbs);
        userProfile.setDailyFatGoal(fats);

        userProfileRepository.save(userProfile);

        log.info("Profile setup for user {}", userProfile.getUser().getEmail());
        return userProfileMapper.toUserResponseDto(userProfile);
    }

    public UserProfileResponseDto getUserProfile(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", userId));

        return userProfileMapper.toUserResponseDto(userProfile);
    }
}
