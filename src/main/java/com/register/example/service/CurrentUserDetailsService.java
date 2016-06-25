package com.register.example.service;

import com.register.example.entity.CurrentUser;
import com.register.example.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CurrentUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public CurrentUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CurrentUser loadUserByUsername(String value) {
        log.info("Autentykacja uzytkownika {}", value);
        try {
            User user = userService.getUserByEmailOrLogin(value)
                    .orElseThrow(() -> new UsernameNotFoundException(String.format("Uzytkownik %s nie istnieje", value)));
            return new CurrentUser(user);
        } catch (Exception e) {
            log.debug(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}