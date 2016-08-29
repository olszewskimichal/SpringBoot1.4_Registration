package com.register.example.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.register.example.forms.EmailRegistrationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class EmailConsumer {
    @JmsListener(destination = "email.queue")
    public void receive(String text) throws IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        log.info("Otrzymalem z kolejki {}",objectMapper.readValue(text, EmailRegistrationDTO.class));
    }
}
