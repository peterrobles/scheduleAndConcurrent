package com.example.webhookdemo.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
class WebHookPersistenceTest {

    private static WebHookPersistence webHookPersistence;

    @BeforeEach // empty persistence on each test
    public void setup() {
        webHookPersistence = new WebHookPersistence();
    }

    @Test
    public void registerSingleWebHook_checkMemoryContainsIt() {
        Long freq = 23L;
        Long providedId = 1234L;
        WebHook webHook = WebHook.builder()
                .id(providedId)
                .frequency(freq)
                .build();
        WebHook actual = webHookPersistence.insert(webHook);
        assertEquals(actual.id(), 1L); // Id is ignored for saving
        assertEquals(actual.frequency(), freq);
    }

    @Test
    public void updateWebHook_checkMemoryContainsNewValue() {
        Long initialFreq = 23L;
        Long providedId = 1234L;
        Long updatedFreq = 4321L;
        WebHook initialWebHook = WebHook.builder()
                .id(providedId)
                .frequency(initialFreq)
                .build();
        WebHook intermediateWebHook = webHookPersistence.insert(initialWebHook);
        WebHook updatedWebHook = webHookPersistence.update(intermediateWebHook.toBuilder().frequency(updatedFreq).build());
        assertEquals(updatedWebHook.id(), 1L);
        assertEquals(updatedWebHook.frequency(), updatedFreq);
    }

    @Test
    public void deregisterSingleWebHook_checkMemoryNotContainIt() {
        Long freq = 23L;
        Long providedId = 1234L;
        WebHook webHook = WebHook.builder()
                .id(providedId)
                .frequency(freq)
                .build();
        WebHook newWebHook = webHookPersistence.insert(webHook);
        webHookPersistence.delete(newWebHook.id());
        WebHook actual = webHookPersistence.get(newWebHook.id());
        assertNull(actual);
    }

    @Test
    public void registerMultipleWebhooksInParallel_checkMemoryHasAll() throws Exception {
        int threads = 20;
        int tasksPerThread = 200;
        int iterations = threads * tasksPerThread;

        ExecutorService service = Executors.newFixedThreadPool(threads);
        SecureRandom random = SecureRandom.getInstanceStrong();
        IntStream.range(0, iterations)
                .<Runnable>mapToObj(i -> () -> {
                    int frequency = random.nextInt(5, 4 * 60 * 60);
                    WebHook webHook = WebHook.builder()
                            .frequency((long) frequency)
                            .url("customer url: " + frequency)
                            .build();
                    webHookPersistence.insert(webHook);
                })
                .forEach(service::submit);
        service.shutdown();
        service.awaitTermination(5, TimeUnit.MINUTES);

        // If no race condition, all elements are added to the "DB"
        assertNotNull(webHookPersistence.get((long) iterations));
        assertNull(webHookPersistence.get((long) iterations + 1));
    }

}
