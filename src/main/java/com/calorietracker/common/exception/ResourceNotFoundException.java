package com.calorietracker.common.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends CalorieTrackerException {
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " not found with id: " + id,
                HttpStatus.NOT_FOUND,
                "RESOURCE_NOT_FOUND");
    }
}
