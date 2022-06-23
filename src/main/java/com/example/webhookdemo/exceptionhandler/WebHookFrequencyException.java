package com.example.webhookdemo.exceptionhandler;

import java.io.Serial;

public class WebHookFrequencyException extends AppException {

    @Serial
    private static final long serialVersionUID = 1L;

    public WebHookFrequencyException(String message) {
        super(message);
    }
}