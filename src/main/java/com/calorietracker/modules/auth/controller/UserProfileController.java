package com.calorietracker.modules.auth.controller;

import com.calorietracker.common.response.ApiResponse;
import com.calorietracker.modules.auth.domain.User;
import com.calorietracker.modules.auth.dto.UserProfileRequestDto;
import com.calorietracker.modules.auth.dto.UserProfileResponseDto;
import com.calorietracker.modules.auth.service.UserProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/profile")
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserProfileResponseDto>> getProfile(HttpServletRequest request) {
        Long userId = getAuthenticatedUserId();
        UserProfileResponseDto userProfileResponseDto = userProfileService.getUserProfile(userId);

        return ResponseEntity.ok(
                ApiResponse.success(userProfileResponseDto,
                        "Profile retrieved",
                        request.getRequestURI())
        );
    }

    @PutMapping("/setup")
    public ResponseEntity<ApiResponse<UserProfileResponseDto>> setUpProfile(
            @Valid @RequestBody UserProfileRequestDto requestDto,
            HttpServletRequest request
            ) {
        Long userId = getAuthenticatedUserId();
        UserProfileResponseDto userProfileResponseDto = userProfileService.setupProfile(userId, requestDto);

        return ResponseEntity.ok(
                ApiResponse.success(userProfileResponseDto,
                        "Profile set up was completed",
                        request.getRequestURI())
        );
    }

    private Long getAuthenticatedUserId() {
        User user = (User) Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .getPrincipal();
        return Objects.requireNonNull(user).getId();
    }
}
