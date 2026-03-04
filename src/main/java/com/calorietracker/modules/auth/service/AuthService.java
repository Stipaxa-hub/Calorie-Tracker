package com.calorietracker.modules.auth.service;

import com.calorietracker.common.exception.DuplicateResourceException;
import com.calorietracker.common.exception.ResourceNotFoundException;
import com.calorietracker.common.security.JwtService;
import com.calorietracker.modules.auth.domain.Role;
import com.calorietracker.modules.auth.domain.SubscriptionTier;
import com.calorietracker.modules.auth.domain.User;
import com.calorietracker.modules.auth.dto.AuthResponseDto;
import com.calorietracker.modules.auth.dto.UserLoginRequestDto;
import com.calorietracker.modules.auth.dto.UserRegisterRequestDto;
import com.calorietracker.modules.auth.mapper.UserMapper;
import com.calorietracker.modules.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AuthResponseDto register(UserRegisterRequestDto registerRequestDto) {
        if (userRepository.existsByEmail(registerRequestDto.email())) {
            throw new DuplicateResourceException(
                    "Email already registered: " + registerRequestDto.email()
            );
        }

        User user = userMapper.toUser(registerRequestDto);
        user.setPassword(passwordEncoder.encode(registerRequestDto.password()));
        user.setRole(Role.USER);
        user.setSubscriptionTier(SubscriptionTier.FREE);

        userRepository.save(user);

        log.info("New user registered: {}", user.getEmail());

        return buildAuthResponse(user);
    }

    public AuthResponseDto login(UserLoginRequestDto requestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.email(), requestDto.password()
                )
        );

        User user = userRepository.findByEmail(requestDto.email()).orElseThrow(() -> new ResourceNotFoundException("User", 0L));

        log.info("User logged in: {}", user.getEmail());
        return buildAuthResponse(user);
    }

    private AuthResponseDto buildAuthResponse(User user) {
        return AuthResponseDto.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .tokenType("Bearer")
                .expiresIn(900L)
                .user(userMapper.toUserSummaryDto(user))
                .build();
    }
}
