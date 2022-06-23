package com.example.webhookdemo.model;

import com.example.webhookdemo.exceptionhandler.ContentNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class WebHookPersistence {

    private final Map<Long, WebHook> memory = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1L);

    public WebHook get(Long id) {
        return memory.get(id);
    }

    public WebHook insert(WebHook webHook) {
        return memory.computeIfAbsent(idSequence.getAndIncrement(), k -> webHook.toBuilder().id(k).build());
    }

    public WebHook update(WebHook webHook) {
        Long id = webHook.id();
        WebHook existingWebHook = memory.get(id);
        if (existingWebHook == null) {
            throw new ContentNotFoundException("Webhook with id: " + id + " didn't exist");
        }
        memory.put(id, webHook);
        return webHook;
    }

    public void delete(Long id) {
        WebHook existingWebHook = memory.remove(id);
        if (existingWebHook == null) {
            throw new ContentNotFoundException("Webhook with id: " + id + " didn't exist");
        }
    }
}
