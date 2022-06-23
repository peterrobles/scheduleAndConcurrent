package com.example.webhookdemo.exceptionhandler;

import java.io.Serial;

public class ContentNotFoundException extends AppException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ContentNotFoundException(String message) {
        super(message);
    }
}