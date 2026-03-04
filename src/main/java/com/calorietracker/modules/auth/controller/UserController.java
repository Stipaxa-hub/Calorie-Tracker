package com.calorietracker.modules.auth.controller;

import com.calorietracker.common.response.ApiResponse;
import com.calorietracker.modules.auth.domain.User;
import com.calorietracker.modules.auth.dto.UserSummaryDto;
import com.calorietracker.modules.auth.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/user")
public class UserController {
    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserSummaryDto>> getCurrentUser(
            HttpServletRequest request
    ) {
        User user = (User) Objects.requireNonNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        ).getPrincipal();

        return ResponseEntity.ok(
                ApiResponse.success(userMapper.toUserSummaryDto(user),
                        "Current user retrieved",
                        request.getRequestURI())
        );
    }
}
