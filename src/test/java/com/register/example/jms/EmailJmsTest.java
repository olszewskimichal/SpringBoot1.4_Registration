package com.register.example.jms;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.register.example.IntegrationTestBase;
import com.register.example.dto.EmailRegistrationDTO;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


public class EmailJmsTest extends IntegrationTestBase {

    @Autowired
    private EmailProducer jmsSender;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Test
    public void testJMSSender() throws JsonProcessingException {
        EmailRegistrationDTO emailRegistrationDTO=new EmailRegistrationDTO("test","test","test");
        Queue queue=new ActiveMQQueue("test");
        jmsSender.setQueue(queue);
        jmsSender.send(emailRegistrationDTO);
        TextMessage msg = (TextMessage) jmsTemplate.receive("test");
        System.out.println("***********************");
        try {
            ObjectMapper objectMapper=new ObjectMapper();
            EmailRegistrationDTO recivedObject=objectMapper.readValue(msg.getText(), EmailRegistrationDTO.class);
            System.out.println(msg.getText());
            assertThat(recivedObject).isEqualToComparingFieldByField(emailRegistrationDTO);

        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("***********************");
    }
}
