package com.register.example.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.register.example.soap.WebServiceOperationLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

@Component
@Slf4j
public class OperationLogProducer {
    private final JmsMessagingTemplate template;
    private Queue queue;

    public OperationLogProducer(JmsMessagingTemplate template) {
        this.template = template;
        this.queue = new ActiveMQQueue("operationLog.queue");
    }

    public void writeAction(WebServiceOperationLog operationLog) throws JsonProcessingException {
        log.info("Wstawiam na kolejke {} obiekt {}", queue, operationLog);
        ObjectMapper objectMapper = new ObjectMapper();
        template.convertAndSend(queue, objectMapper.writeValueAsString(operationLog));
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }
}
