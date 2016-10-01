package com.register.example.jms;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.register.example.IntegrationTestBase;
import com.register.example.soap.WebServiceOperationLog;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class OperationLogJmsTest extends IntegrationTestBase {

    @Autowired
    private OperationLogProducer jmsSender;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Test
    public void testJMSSender() throws JsonProcessingException {
        WebServiceOperationLog webServiceOperationLog=new WebServiceOperationLog();
        webServiceOperationLog.setMsgId("testoweId");
        webServiceOperationLog.setDate(new Date());
        webServiceOperationLog.setSuccess(true);

        Queue queue=new ActiveMQQueue("test");
        jmsSender.setQueue(queue);
        jmsSender.writeAction(webServiceOperationLog);
        TextMessage msg = (TextMessage) jmsTemplate.receive("test");
        System.out.println("***********************");
        try {
            ObjectMapper objectMapper=new ObjectMapper();
            WebServiceOperationLog recivedObject=objectMapper.readValue(msg.getText(), WebServiceOperationLog.class);
            System.out.println(msg.getText());
            assertThat(recivedObject).isEqualToComparingFieldByField(webServiceOperationLog);

        } catch (JMSException | IOException e) {
            e.printStackTrace();
        }
        System.out.println("***********************");
    }
}
