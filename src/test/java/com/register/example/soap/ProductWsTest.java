package com.register.example.soap;

import com.register.example.IntegrationTestBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import javax.xml.soap.MessageFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;


public class ProductWsTest extends IntegrationTestBase {
    @Autowired
    private ProductWS ws;

    private WebServiceTemplate webServiceTemplate;

    public void setDefaultUri() {
        webServiceTemplate.setDefaultUri("http://localhost:8888/services/ProductWS");
    }

    @Before
    public void startServer() throws Exception {
        SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory(
                MessageFactory.newInstance());
        messageFactory.afterPropertiesSet();
        webServiceTemplate = new WebServiceTemplate(messageFactory);

        webServiceTemplate.setCheckConnectionForError(false);
        webServiceTemplate.setCheckConnectionForFault(false);

    }

    @Test
    public void shouldReturnTest() {
        String test = ws.testWebservice(null);
        assertThat(test).isEqualTo("TEST");
    }

    @Test
    public void testGetProducts() throws Exception {
        StreamSource source = new StreamSource(new StringReader("<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><S:Body><ns2:getProducts xmlns:ns2=\"http://soap.example.register.com/\"><arg0 productLimit=\"4\"><RequestHeaderWS source=\"DUPA\"/></arg0></ns2:getProducts></S:Body></S:Envelope>\n"));
        StringWriter w=new StringWriter();
        StreamResult result = new StreamResult(w);

        Boolean results=webServiceTemplate.sendSourceAndReceiveToResult("http://localhost:8888/services/ProductWS",
                source, result);
        assertThat(results).isTrue();
    }

    @Test
    public void testAddProducts() throws Exception {
        StreamSource source = new StreamSource(new StringReader("<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><S:Body><ns2:addProducts xmlns:ns2=\"http://soap.example.register.com/\"><arg0><RequestHeaderWS messageId=\"f92b77ad-45d2-4c7e-8e84-ca0af177c650\" source=\"DUPA\"/><products><description>desc</description><name>Produkt_namece88</name><price>0</price></products><products><description>desc</description><name>Produkt_name1779</name><price>10</price></products><products><description>desc</description><name>Produkt_namec795</name><price>20</price></products><products><description>desc</description><name>Produkt_name7c3d</name><price>30</price></products><products><description>desc</description><name>Produkt_name8667</name><price>40</price></products></arg0></ns2:addProducts></S:Body></S:Envelope>"));
        StringWriter w=new StringWriter();
        StreamResult result = new StreamResult(w);

        Boolean results=webServiceTemplate.sendSourceAndReceiveToResult("http://localhost:8888/services/ProductWS",
                source, result);
        assertThat(results).isTrue();
    }

}
