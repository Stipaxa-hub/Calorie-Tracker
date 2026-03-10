package com.calorietracker.modules.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserSummaryDto {
    private Long id;
    private String fullName;
    private String email;
    private String role;
    private String subscriptionTier;
    private Boolean profileComplete;
}
