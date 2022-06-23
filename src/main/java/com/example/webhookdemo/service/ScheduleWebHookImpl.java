package com.example.webhookdemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@RequiredArgsConstructor
@Slf4j
@Component
public class ScheduleWebHookImpl implements ScheduleWebHook {

    private final TaskScheduler taskScheduler;

    Map<Long, ScheduledFuture<?>> jobsMap = new ConcurrentHashMap<>();

    public void removeScheduledTask(@NonNull Long id) {
        ScheduledFuture<?> scheduledTask = jobsMap.get(id);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.remove(id);
        }
    }

    public void scheduleATask(Long id, Runnable tasklet, String cronExpression) {
        log.info("Scheduling task for webhook id: {} and cron expression: {}", id, cronExpression);
        // remove previous tasks if exists, so new cron takes effect
        removeScheduledTask(id);
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet, new CronTrigger(cronExpression, TimeZone.getTimeZone(TimeZone.getDefault().getID())));
        jobsMap.put(id, scheduledTask);
    }

    @PreDestroy
    public void destroy() {
        log.info("Destroying scheduling service");
        jobsMap.forEach((k, v) -> removeScheduledTask(k));
    }

}
