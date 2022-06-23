package com.example.webhookdemo.service;

import com.example.webhookdemo.model.WebHook;
import io.netty.handler.logging.LogLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.time.LocalDateTime;

@Slf4j
@Builder
public record ScheduleTask(WebHook webHook) implements Runnable {

    @Override
    public void run() {
        log.info("Running action for webhook: {}", webHook);
        HttpClient httpClient = HttpClient
                .create()
                .wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(webHook.url())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String responseS = client.post()
                .bodyValue(LocalDateTime.now()) // TODO: add custom object
                .exchangeToMono(response -> {
                    if (response.statusCode().isError()) {
                        Mono<String> apiResponse = response.bodyToMono(String.class);
                        return Mono.error(new Exception("Error occurred while accessing webhook url. Code: " + response.statusCode() + " response: " + apiResponse));
                    }
                    return response.bodyToMono(String.class);
                })
                .block();

        log.info("Response is: {}", responseS);
    }
}
