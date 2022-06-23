package com.example.webhookdemo.service;

import com.example.webhookdemo.dto.WebHookDTO;

public interface WebHookService {

    WebHookDTO registerWebHook(WebHookDTO webHook);
    WebHookDTO updateFrequency(WebHookDTO webHook);
    void deregisterWebHook(Long webHookId);

}
