package com.calorietracker.modules.auth.dto;

import com.calorietracker.modules.auth.validator.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequestDto(
        @NotBlank(message = "Full name is required")
        @Size(min = 2, max = 100)
        String fullName,
        @NotBlank(message = "Email is required")
        @Email(message = "Must be a valid email address")
        String email,
        @NotBlank(message = "Password is required")
        @Password
        String password
) {
}
