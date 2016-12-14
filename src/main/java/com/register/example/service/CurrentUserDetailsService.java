package com.register.example.service;

import com.register.example.entity.CurrentUser;
import com.register.example.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@Service
@Slf4j
@Profile("!test")
public class CurrentUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final LoginAttemptService loginAttemptService;
    private final HttpServletRequest request;
    private final Environment env;

    @Autowired
    public CurrentUserDetailsService(UserService userService, LoginAttemptService loginAttemptService, HttpServletRequest request, Environment env) {
        this.userService = userService;
        this.loginAttemptService = loginAttemptService;
        this.request = request;
        this.env = env;
    }

    @Override
    public CurrentUser loadUserByUsername(String value) {
        String profiles[] = env.getActiveProfiles();
        String ip;
        if (Arrays.binarySearch(profiles, "integrationTest") >= 0) {
            ip = "ipTestowe";
        } else ip = getClientIP();

        log.info("Autentykacja uzytkownika {} z ip {}", value, ip);

        if (loginAttemptService.isBlocked(ip)) {
            log.info("Zbyt duża ilość błednych logowań - Powinno zablokować");
            throw new RuntimeException("blocked");
        }

        User user = userService.getUserByEmailOrLogin(value)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Uzytkownik %s nie istnieje", value)));
        return new CurrentUser(user);
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

}