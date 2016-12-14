package com.register.example.service;

import com.register.example.IntegrationTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


public class LoginAttemptServiceTest extends IntegrationTestBase {

    @Autowired
    LoginAttemptService loginAttemptService;


    @Test
    public void shouldBlockUserAndActivateAfterSuccesLogin() throws Exception {
        loginAttemptService.loginFailed("login1");
        loginAttemptService.loginFailed("login1");
        loginAttemptService.loginFailed("login1");
        assertThat(loginAttemptService.isBlocked("login1")).isTrue();
        assertThat(loginAttemptService.getAttempts5minCache().get("login1").intValue()).isEqualTo(3);
        assertThat(loginAttemptService.getAttemptsDayCache().get("login1").intValue()).isEqualTo(3);
        assertThat(loginAttemptService.getAttemptsHourCache().get("login1").intValue()).isEqualTo(3);
        loginAttemptService.loginSucceeded("login1");
        assertThat(loginAttemptService.isBlocked("login1")).isFalse();
        assertThat(loginAttemptService.getAttempts5minCache().get("login1").intValue()).isEqualTo(0);
        assertThat(loginAttemptService.getAttemptsDayCache().get("login1").intValue()).isEqualTo(0);
        assertThat(loginAttemptService.getAttemptsHourCache().get("login1").intValue()).isEqualTo(0);

    }
}