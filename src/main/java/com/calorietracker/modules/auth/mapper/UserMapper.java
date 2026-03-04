package com.calorietracker.modules.auth.mapper;

import com.calorietracker.modules.auth.domain.Role;
import com.calorietracker.modules.auth.domain.SubscriptionTier;
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
            qualifiedByName = "roleToString")
    @Mapping(source = "subscriptionTier", target = "subscriptionTier",
            qualifiedByName = "tierToString")
    UserSummaryDto toUserSummaryDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "subscriptionTier", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUser(UserRegisterRequestDto userRegisterRequestDto);

    @Named("roleToString")
    default String roleToString(Role role) {
        return role != null ? role.name() : null;
    }

    @Named("tierToString")
    default String tierToString(SubscriptionTier tier) {
        return tier != null ? tier.name() : null;
    }
}
