package com.calorietracker.common.exception;

import org.springframework.http.HttpStatus;

public class ExternalServiceException extends CalorieTrackerException {
    public ExternalServiceException(String service, String reason) {
        super("External service '" + service + "' failed: " + reason,
                HttpStatus.BAD_GATEWAY,
                "EXTERNAL_SERVICE_ERROR");
    }
}
