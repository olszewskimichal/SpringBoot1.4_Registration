package com.register.example.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.register.example.repository.OperationLogRepository;
import com.register.example.soap.WebServiceOperationLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@Profile("!test")
public class OperationLogConsumer {
    private final OperationLogRepository operationLogRepository;

    @Autowired
    public OperationLogConsumer(OperationLogRepository operationLogRepository) {
        this.operationLogRepository = operationLogRepository;
    }

    @JmsListener(destination = "operationLog.queue")
    public void receive(String text) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        WebServiceOperationLog webServiceOperationLog = objectMapper.readValue(text, WebServiceOperationLog.class);
        log.info("Otrzymalem z kolejki {}", webServiceOperationLog);
        operationLogRepository.save(webServiceOperationLog);
    }
}
