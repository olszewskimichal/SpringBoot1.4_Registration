package com.register.example.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LoginAttemptService {
    private final int MAX_ATTEMPT_DAY=8;
    private final int MAX_ATTEMPT_HOUR=5;
    private final int MAX_ATTEMPT_5MIN=3;
    private LoadingCache<String,Integer> attemptsDayCache;
    private LoadingCache<String,Integer> attemptsHourCache;
    private LoadingCache<String,Integer> attempts5minCache;

    public LoginAttemptService() {
        super();
        attemptsDayCache = CacheBuilder.newBuilder().
                expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
            public Integer load(String key) {
                return 0;
            }
        });
        attemptsHourCache = CacheBuilder.newBuilder().
                expireAfterWrite(1, TimeUnit.HOURS).build(new CacheLoader<String, Integer>() {
            public Integer load(String key) {
                return 0;
            }
        });
        attempts5minCache = CacheBuilder.newBuilder().
                expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
            public Integer load(String key) {
                return 0;
            }
        });
    }

    public void loginSucceeded(String key) {
        attemptsDayCache.invalidate(key);
        attemptsHourCache.invalidate(key);
        attempts5minCache.invalidate(key);
    }

    public void loginFailed(String key) {
        int attemptsDay;
        int attemptsHour;
        int attempts5min;
        try {
            attemptsDay = attemptsDayCache.get(key);
            attemptsHour = attemptsHourCache.get(key);
            attempts5min = attempts5minCache.get(key);
        } catch (ExecutionException e) {
            attemptsDay = 0;
            attemptsHour = 0;
            attempts5min = 0;
        }
        attemptsDay++;
        attemptsHour++;
        attempts5min++;
        attemptsDayCache.put(key, attemptsDay);
        attemptsHourCache.put(key, attemptsHour);
        attempts5minCache.put(key, attempts5min);
    }

    public boolean isBlocked(String key) {
        try {
            boolean blocked=false;
            if (attempts5minCache.get(key)>=MAX_ATTEMPT_5MIN){
                    log.info("blokada na 5min");
                    blocked=true;
            }
            else if (attemptsHourCache.get(key)>=MAX_ATTEMPT_HOUR){
                     log.info("blokada na 1h");
                     blocked=true;
            }
            else if (attemptsDayCache.get(key)>=MAX_ATTEMPT_DAY){
                log.info("Blokada na cały dzien");
                blocked=true;
            }
            return blocked;
        } catch (ExecutionException e) {
            return false;
        }
    }

}
