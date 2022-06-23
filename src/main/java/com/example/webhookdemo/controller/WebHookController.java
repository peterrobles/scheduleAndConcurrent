package com.example.webhookdemo.controller;

import com.example.webhookdemo.dto.WebHookDTO;
import com.example.webhookdemo.exceptionhandler.WebHookConflictException;
import com.example.webhookdemo.service.WebHookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/webhook")
@Slf4j
@RequiredArgsConstructor
public class WebHookController {

    private final WebHookService webHookService;

    @PostMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register new webhook")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operation OK"),
            @ApiResponse(responseCode = "416", description = "Frequency out of range")
    })
    public WebHookDTO registerWebHook(@RequestBody WebHookDTO webhook) {
        return webHookService.registerWebHook(webhook);
    }

    @DeleteMapping(path = "/{id}/deregister", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Deregister webhook by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operation OK"),
            @ApiResponse(responseCode = "404", description = "Webhook with id doesn't exist")
    })
    public void deregisterWebHook(@PathVariable Long id) {
        webHookService.deregisterWebHook(id);
    }

    @PutMapping(path = "/{id}/frequency", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Change frequency of callbacks between 5 seconds and 4 hours")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operation OK"),
            @ApiResponse(responseCode = "409", description = "Mismatch provided info"),
            @ApiResponse(responseCode = "404", description = "Webhook with id doesn't exist"),
            @ApiResponse(responseCode = "416", description = "Frequency out of range")
    })
    public WebHookDTO changeFrequencyWebHook(@PathVariable Long id, @RequestBody WebHookDTO webHook) {
        if (!id.equals(webHook.id())) {
            throw new WebHookConflictException("Id provided in request doesn't match with payload");
        }
        return webHookService.updateFrequency(webHook);
    }
}
