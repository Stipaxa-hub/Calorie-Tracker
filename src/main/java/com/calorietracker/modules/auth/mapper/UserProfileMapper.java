package com.calorietracker.modules.auth.mapper;

import com.calorietracker.modules.auth.domain.UserProfile;
import com.calorietracker.modules.auth.dto.UserProfileRequestDto;
import com.calorietracker.modules.auth.dto.UserProfileResponseDto;
import java.time.LocalDate;
import java.time.Period;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserProfileMapper {
    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(target = "age", expression = "java(calculateAge(userProfile))")
    @Mapping(source = "gender", target = "gender",
            qualifiedByName = "enumToString")
    @Mapping(source = "activityLevel", target = "activityLevel",
            qualifiedByName = "enumToString")
    @Mapping(source = "goalType", target = "goalType",
            qualifiedByName = "enumToString")
    UserProfileResponseDto toUserResponseDto(UserProfile userProfile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "profileComplete", ignore = true)
    @Mapping(target = "dailyCalorieGoal", ignore = true)
    @Mapping(target = "dailyProteinGoal", ignore = true)
    @Mapping(target = "dailyCarbGoal", ignore = true)
    @Mapping(target = "dailyFatGoal", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserProfile toUserProfile(UserProfileRequestDto userProfileRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "profileComplete", ignore = true)
    @Mapping(target = "dailyCalorieGoal", ignore = true)
    @Mapping(target = "dailyProteinGoal", ignore = true)
    @Mapping(target = "dailyCarbGoal", ignore = true)
    @Mapping(target = "dailyFatGoal", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(UserProfileRequestDto requestDto,
                      @MappingTarget UserProfile userProfile);

    @Named("enumToString")
    default String enumToString(Enum<?> value) {
        return value != null ? value.name() : null;
    }

    default Integer calculateAge(UserProfile profile) {
        if (profile.getDateOfBirth() == null) return null;
        return Period.between(
                profile.getDateOfBirth(),
                LocalDate.now()
        ).getYears();
    }
}
