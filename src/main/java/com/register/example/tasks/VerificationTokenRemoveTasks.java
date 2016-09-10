package com.register.example.tasks;

import com.register.example.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class VerificationTokenRemoveTasks {

    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public VerificationTokenRemoveTasks(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Scheduled(
            cron = "00 39 18 * * *")
    public void cronJob() {
        Integer deleted = verificationTokenRepository.deleteVerificationTokenByExpiryDateLessThen(LocalDateTime.now().minusDays(7));
        log.info(String.format("Usunieto %d tokenow", deleted));
    }
}
