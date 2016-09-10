package com.register.example.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.register.example.dto.EmailRegistrationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

@Component
@Slf4j
public class EmailProducer {
    private final JmsMessagingTemplate template;
    private Queue queue;

    public EmailProducer(JmsMessagingTemplate template, Queue queue) {
        this.template = template;
        this.queue = queue;
    }

    public void send(EmailRegistrationDTO emailRegistrationDTO) throws JsonProcessingException {
        log.info("Wstawiam na kolejke {} obiekt {}", queue, emailRegistrationDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        template.convertAndSend(queue, objectMapper.writeValueAsString(emailRegistrationDTO));
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }
}
