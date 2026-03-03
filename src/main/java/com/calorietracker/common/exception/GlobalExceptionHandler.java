package com.calorietracker.common.exception;

import com.calorietracker.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // Handles all our custom exceptions
    @ExceptionHandler(CalorieTrackerException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleCalorieTrackerException(
            CalorieTrackerException ex, HttpServletRequest request) {

        log.error("Application exception [{}]: {}", ex.getErrorCode(), ex.getMessage());

        Map<String, String> errorData = Map.of("errorCode", ex.getErrorCode());

        return ResponseEntity
                .status(ex.getStatus())
                .body(ApiResponse.error(ex.getMessage(), errorData, request.getRequestURI()));
    }

    // Handles Spring Validation failures (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, List<String>>>> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.toList());

        log.warn("Validation failed: {}", errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Validation failed",
                        Map.of("errors", errors),
                        request.getRequestURI()));
    }

    // Catches anything we didn't explicitly handle
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception ex, HttpServletRequest request) {

        log.error("Unhandled exception at {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        "An unexpected error occurred. Please try again later.",
                        null,
                        request.getRequestURI()));
    }

}
