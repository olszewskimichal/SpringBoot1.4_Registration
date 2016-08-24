package com.register.example.service;

import com.register.example.entity.CurrentUser;
import com.register.example.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CurrentUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final LoginAttemptService loginAttemptService;
    private final HttpServletRequest request;

    @Autowired
    public CurrentUserDetailsService(UserService userService, LoginAttemptService loginAttemptService, HttpServletRequest request) {
        this.userService = userService;
        this.loginAttemptService = loginAttemptService;
        this.request = request;
    }

    @Override
    public CurrentUser loadUserByUsername(String value) {
        String ip = getClientIP();

        log.info("Autentykacja uzytkownika {} z ip {}", value,ip);

        if (loginAttemptService.isBlocked(ip)) {
            log.info("Zbyt duża ilość błednych logowań - blokada do jutra");
            throw new RuntimeException("blocked");
        }

        User user = userService.getUserByEmailOrLogin(value)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Uzytkownik %s nie istnieje", value)));
        return new CurrentUser(user);
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

}