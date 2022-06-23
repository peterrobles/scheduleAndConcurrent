package com.example.webhookdemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/clock")
@Slf4j
@RequiredArgsConstructor
public class ClockController {

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public void registerWebHook(@RequestBody LocalDateTime currentTime) {
        log.info("Current time received is: {}", currentTime);
    }
}
