package com.calorietracker.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CalorieTrackerException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }
}
