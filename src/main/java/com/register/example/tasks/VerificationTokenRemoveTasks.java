package com.register.example.tasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class VerificationTokenRemoveTasks {

    @Scheduled(
            cron = "40 54 23 * * *")
    public void cronJob() {
        System.out.println("There are greetings in the data store."+ LocalDateTime.now());
    }
}
