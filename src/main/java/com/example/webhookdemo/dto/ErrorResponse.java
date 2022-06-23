package com.example.webhookdemo.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class ErrorResponse {
    @Builder.Default
    LocalDateTime timestamp = LocalDateTime.now();
    String message;
    String details;
}