package com.example.AlertIT.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record ErrorResponse(
        @JsonProperty("timestamp") LocalDateTime timestamp,
        @JsonProperty("status") int status,
        @JsonProperty("error") String error,
        @JsonProperty("message") String message,
        @JsonProperty("path") String path
) {
    public static ErrorResponse create(int status, String error, String message, String path) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status,
                error,
                message,
                path
        );
    }
}
