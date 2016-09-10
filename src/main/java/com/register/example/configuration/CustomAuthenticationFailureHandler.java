package com.register.example.configuration;

import com.register.example.service.LoginAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component
@Slf4j
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private MessageSource messages;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LoginAttemptService loginAttemptService;
    @Autowired
    private LocaleResolver localeResolver;


    @Override
    public void onAuthenticationFailure(javax.servlet.http.HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        logger.info("Bledna autoryzacja uzytkownika");
        logger.info(exception.getMessage());
        setDefaultFailureUrl("/login-error");
        super.onAuthenticationFailure(request, response, exception);
        Locale locale = localeResolver.resolveLocale(request);

        if (exception instanceof BadCredentialsException) {
            String email = request.getParameter("username");
            String ip = getClientIP();

            if (loginAttemptService.isBlocked(ip)) {
                log.info("Zbyt duża ilość błednych logowań na konto {} - Blokujemy na 1 dzień", email);
            }

        }
        String errorMessage = messages.getMessage("message.badCredentials", null, locale);

        if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
            errorMessage = messages.getMessage("auth.message.disabled", null, locale);
        } else if (exception.getMessage().equalsIgnoreCase("User account has expired")) {
            errorMessage = messages.getMessage("auth.message.expired", null, locale);
        } else if (exception.getMessage().equalsIgnoreCase("blocked")) {
            errorMessage = messages.getMessage("auth.message.blocked", null, locale);
        }
        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
    }


    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
