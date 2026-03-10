package com.calorietracker.modules.auth.controller;

import com.calorietracker.common.response.ApiResponse;
import com.calorietracker.modules.auth.dto.AuthResponseDto;
import com.calorietracker.modules.auth.dto.UserLoginRequestDto;
import com.calorietracker.modules.auth.dto.UserRegisterRequestDto;
import com.calorietracker.modules.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDto>> register(
            @Valid @RequestBody UserRegisterRequestDto registerRequestDto,
            HttpServletRequest httpServletRequest
    ) {
        AuthResponseDto authResponse = authService.register(registerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                                authResponse, "Registration successful", httpServletRequest.getRequestURI()
                        )
                );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(
            @Valid @RequestBody UserLoginRequestDto loginRequestDto,
            HttpServletRequest httpServletRequest
    ) {
        AuthResponseDto authResponse = authService.login(loginRequestDto);
        return ResponseEntity.ok(ApiResponse.success(authResponse,
                "Login successful",
                httpServletRequest.getRequestURI()));
    }


}
