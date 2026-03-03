package com.calorietracker.common.exception;

import org.springframework.http.HttpStatus;

public class SubscriptionRequiredException extends CalorieTrackerException {
    public SubscriptionRequiredException(String feature) {
        super("Feature '" + feature + "' requires a Premium subscription",
                HttpStatus.PAYMENT_REQUIRED,
                "SUBSCRIPTION_REQUIRED");
    }
}
