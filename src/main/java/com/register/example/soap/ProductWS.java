package com.register.example.soap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.register.example.service.ProductService;
import com.register.example.soap.objects.AddProductsRequestWS;
import com.register.example.soap.objects.AddProductsResponseWS;
import com.register.example.soap.objects.GetProductsRequestWS;
import com.register.example.soap.objects.GetProductsResponseWS;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@Component
@WebService(serviceName = "ProductWS")
@Slf4j
@NoArgsConstructor
@HandlerChain(file = "handler-chain.xml")
@Profile("!test")
public class ProductWS {
    private ProductService productService;

    @Autowired
    public ProductWS(ProductService productService) {
        this.productService = productService;
    }

    @WebMethod(operationName = "addProducts", action = "addProducts")
    public AddProductsResponseWS addProducts(@WebParam AddProductsRequestWS productsRequest) {
        log.info("WS - dodajProdukt");
        try {
            return productService.addProducts(productsRequest);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }
        //ku chwale kompilatora
        return null;
    }

    @WebMethod(operationName = "testWebservice", action = "testWebservice")
    public String testWebservice(String test) {
        log.info("WS - test");
        return "TEST";
    }

    @WebMethod(operationName = "getProducts", action = "getProducts")
    public GetProductsResponseWS getProducts(@WebParam GetProductsRequestWS requestWS) {
        log.info("WS - getProducts");
        try {
            return productService.getProducts(requestWS);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }
        //ku chwale kompilatora
        return null;
    }

}
