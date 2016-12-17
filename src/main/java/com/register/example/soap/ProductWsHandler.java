package com.register.example.soap;

import lombok.extern.slf4j.Slf4j;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ProductWsHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public boolean handleMessage(SOAPMessageContext context) {

        Boolean outBoundProperty = (Boolean) context
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        // if this is an incoming message from the client
        if (!outBoundProperty) {

            try {
                SOAPMessage soapMsg = context.getMessage();
                soapMsg.writeTo(System.out);
            } catch (SOAPException | IOException e) {
                log.debug(e.toString());
            }

        }

        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        log.debug("Server : handleFault()......");
        return true;
    }

    @Override
    public void close(MessageContext context) {

    }


    @Override
    public Set<QName> getHeaders() {
        return new HashSet<>();
    }
}