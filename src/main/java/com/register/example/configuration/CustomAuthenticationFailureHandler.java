package com.register.example.configuration;

import com.register.example.service.LoginAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private MessageSource messages;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LoginAttemptService loginAttemptService;




    @Override
    public void onAuthenticationFailure(javax.servlet.http.HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        logger.info("Bledna autoryzacja uzytkownika");
        logger.info(exception.getMessage());
        if (exception instanceof BadCredentialsException) {
            String email = request.getParameter("username");
            String ip = getClientIP();

            if (loginAttemptService.isBlocked(ip)) {
                log.info("Zbyt duża ilość błednych logowań na konto {} - Blokujemy na 1 dzień",email);
            }

        }
    }



    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
