package com.register.example.configuration;

import com.register.example.service.LoginAttemptService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {
    private final LoginAttemptService loginAttemptService;

    //@Autowired - >When SpringBoot<1.4.0
    public AuthenticationSuccessListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
        WebAuthenticationDetails auth = (WebAuthenticationDetails)
                authenticationSuccessEvent.getAuthentication().getDetails();

        loginAttemptService.loginSucceeded(auth.getRemoteAddress());
    }
}
