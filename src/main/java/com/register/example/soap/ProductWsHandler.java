package com.register.example.soap;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.IOException;
import java.util.Set;

public class ProductWsHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public boolean handleMessage(SOAPMessageContext context) {

        //System.out.println("Server executing SOAP Handler");

        Boolean outBoundProperty = (Boolean) context
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        // if this is an incoming message from the client
        if (!outBoundProperty) {

            try {
                SOAPMessage soapMsg = context.getMessage();
                soapMsg.writeTo(System.out);
            } catch (SOAPException | IOException e) {
                System.err.println(e);
            }

        }

        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        System.out.println("Server : handleFault()......");
        return true;
    }

    @Override
    public void close(MessageContext context) {
       // System.out.println("Server : close()......");
    }


    @Override
    public Set<QName> getHeaders() {
        return null;
    }
}