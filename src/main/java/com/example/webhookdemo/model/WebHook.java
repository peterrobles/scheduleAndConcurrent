package com.example.webhookdemo.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record WebHook(Long id, String url, Long frequency /* in seconds */) {
}
