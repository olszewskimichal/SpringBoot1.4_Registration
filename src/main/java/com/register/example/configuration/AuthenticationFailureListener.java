package com.register.example.configuration;

import com.register.example.service.LoginAttemptService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {


    private final LoginAttemptService loginAttemptService;

    //@Autowired - >When SpringBoot<1.4.0
    public AuthenticationFailureListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent authenticationFailureBadCredentialsEvent) {
        WebAuthenticationDetails auth = (WebAuthenticationDetails)
                authenticationFailureBadCredentialsEvent.getAuthentication().getDetails();

        loginAttemptService.loginFailed(auth.getRemoteAddress());
    }
}
