package com.calorietracker.modules.auth.mapper;

import com.calorietracker.modules.auth.domain.User;
import com.calorietracker.modules.auth.dto.UserRegisterRequestDto;
import com.calorietracker.modules.auth.dto.UserSummaryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    @Mapping(source = "role", target = "role",
            qualifiedByName = "enumToString")
    @Mapping(source = "subscriptionTier", target = "subscriptionTier",
            qualifiedByName = "enumToString")
    @Mapping(target = "profileComplete", ignore = true)
    UserSummaryDto toUserSummaryDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "subscriptionTier", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUser(UserRegisterRequestDto userRegisterRequestDto);

    @Named("enumToString")
    default String enumToString(Enum<?> value) {
        return value != null ? value.name() : null;
    }
}
