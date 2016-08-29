package com.register.example.jms;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;

@Configuration
public class JmsConfiguration {
    @Bean
    public Queue queue() {
        return new ActiveMQQueue("email.queue");
    }
}
