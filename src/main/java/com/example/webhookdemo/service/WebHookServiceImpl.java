package com.example.webhookdemo.service;

import com.example.webhookdemo.dto.WebHookDTO;
import com.example.webhookdemo.exceptionhandler.WebHookFrequencyException;
import com.example.webhookdemo.model.WebHook;
import com.example.webhookdemo.model.WebHookPersistence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebHookServiceImpl implements WebHookService {

    private final WebHookPersistence persistence;
    private final ScheduleWebHook scheduleWebHook;

    // Frequency range could be added as a config variable
    private final Long minFrequency = 5L; // 5 seconds
    private final Long maxFrequency = 4L * 60L * 60L; // 4 hours

    public WebHookDTO registerWebHook(WebHookDTO webHook) {
        Long freq = webHook.frequency();
        if (freq < minFrequency || freq > maxFrequency) {
            throw new WebHookFrequencyException("Frequency " + freq + " is out of range");
        }
        WebHook newWebHook = persistence.insert(WebHook.builder()
                .frequency(webHook.frequency())
                .url(webHook.url())
                .build());
        scheduleWebHook.scheduleATask(newWebHook.id(), ScheduleTask.builder()
                .webHook(newWebHook)
                .build(), "*/" + freq + " * * * * * ");
        return WebHookDTO.builder()
                .id(newWebHook.id())
                .frequency(newWebHook.frequency())
                .url(newWebHook.url())
                .build();
    }

    public WebHookDTO updateFrequency(WebHookDTO webHook) {
        Long freq = webHook.frequency();
        if (freq < minFrequency || freq > maxFrequency) {
            throw new WebHookFrequencyException("Frequency " + freq + " is out of range");
        }
        WebHook updatedWebHook = persistence.update(WebHook.builder()
                .id(webHook.id())
                .frequency(freq)
                .url(webHook.url())
                .build());

        scheduleWebHook.scheduleATask(updatedWebHook.id(), ScheduleTask.builder()
                .webHook(updatedWebHook)
                .build(), "*/" + freq + " * * * * * ");
        return WebHookDTO.builder()
                .id(updatedWebHook.id())
                .frequency(updatedWebHook.frequency())
                .url(updatedWebHook.url())
                .build();
    }

    public void deregisterWebHook(Long webHookId) {
        persistence.delete(webHookId);
        scheduleWebHook.removeScheduledTask(webHookId);
    }
}
