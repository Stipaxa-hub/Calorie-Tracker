package com.calorietracker.common.exception;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends CalorieTrackerException{
    public DuplicateResourceException(String message) {
        super(message, HttpStatus.CONFLICT, "DUPLICATE_RESOURCE");
    }
}
