package com.example.webhookdemo.exceptionhandler;

import java.io.Serial;

public class WebHookConflictException extends AppException {

    @Serial
    private static final long serialVersionUID = 1L;

    public WebHookConflictException(String message) {
        super(message);
    }
}