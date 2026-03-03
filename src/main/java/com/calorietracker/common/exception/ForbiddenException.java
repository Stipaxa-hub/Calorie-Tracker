package com.calorietracker.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends CalorieTrackerException {
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN, "FORBIDDEN");
    }
}
