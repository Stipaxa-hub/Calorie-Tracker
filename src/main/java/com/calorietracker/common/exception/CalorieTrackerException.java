package com.calorietracker.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CalorieTrackerException extends RuntimeException {
    private final HttpStatus status;
    private final String errorCode;

    public CalorieTrackerException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}
