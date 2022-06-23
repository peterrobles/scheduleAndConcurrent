package com.example.webhookdemo.service;

import org.springframework.lang.NonNull;

public interface ScheduleWebHook {

    void removeScheduledTask(@NonNull Long id);
    void scheduleATask(Long id, Runnable tasklet, String cronExpression);
}
