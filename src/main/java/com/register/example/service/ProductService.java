package com.register.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.register.example.dto.ProductDTO;
import com.register.example.entity.Product;
import com.register.example.jms.OperationLogProducer;
import com.register.example.repository.ProductRepository;
import com.register.example.soap.WebServiceOperationLog;
import com.register.example.soap.objects.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Profile("!test")
public class ProductService {
    private static int PAGE_LIMIT = 20;
    private static int FIRST_PAGE = 0;
    private static String NAME = "";
    private static String DEFAULT_SORT_BY_NAME = "name";
    private static BigDecimal PRICE_MIN = BigDecimal.ZERO;
    private static BigDecimal PRICE_MAX = BigDecimal.valueOf(Long.MAX_VALUE);


    private final ProductRepository productRepository;

    private final OperationLogProducer logProducer;

    @Autowired
    public ProductService(ProductRepository productRepository, OperationLogProducer logProducer) {
        this.productRepository = productRepository;
        this.logProducer = logProducer;
    }


    public List<ProductDTO> getProducts(Integer size, Integer page, String sort) {
        return getProductsFromRepo(size,page,sort).stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    public List<Product> getProductsFromRepo(Integer size, Integer page, String sort) {
        log.info(String.format("getProducts size= %d page %d sort %s", size, page, sort));
        PageRequest pageRequest = new PageRequest(setPageSize(page), setReturnSize(size), setSortDirection(sort), DEFAULT_SORT_BY_NAME);
        return productRepository.findAll(pageRequest).getContent();
    }

    public ProductDTO getProduct(final Long productId) {
        log.info("ProductService getProduct o id" + productId);
        //TODO obsluzyc orElse
        return new ProductDTO(productRepository.findProductById(productId).get());
    }

    public void deleteProduct(final Long id) {
        productRepository.delete(id);
    }


    public Product createProduct(final ProductDTO productDomain) {
        return productRepository.save(new Product(
                productDomain.getName(),
                productDomain.getImageUrl(),
                productDomain.getDescription(),
                productDomain.getPrice()));
    }

    public void updateProduct(final Long productId, final ProductDTO productDomain) {
        productRepository.updateProduct(
                productDomain.getImageUrl(),
                productDomain.getDescription(),
                productDomain.getName(),
                productDomain.getPrice(),
                productId);
    }

    private int setReturnSize(final Integer size) {
        return (Objects.isNull(size) ? PAGE_LIMIT : size);
    }

    private int setPageSize(final Integer page) {
        return (Objects.isNull(page) ? FIRST_PAGE : page);
    }

    private Sort.Direction setSortDirection(final String sort) {
        return (StringUtils.isEmpty(sort) ? null : Sort.Direction.fromString(sort));
    }

    @Transactional
    @Modifying
    public AddProductsResponseWS addProducts(AddProductsRequestWS productsRequest) throws JsonProcessingException {
        AddProductsResponseWS addProductsResponseWS = new AddProductsResponseWS();
        ResponseHeaderWS responseHeader = new ResponseHeaderWS();
        WebServiceOperationLog webServiceOpLog=new WebServiceOperationLog();
        try {
            responseHeader.setSource("SERWER_GLOWNY");
            HeaderWS headerWS = productsRequest.getHeaderWS();
            webServiceOpLog.setDate(new Date());
            if (headerWS.getMessageId()==null) throw new NullPointerException("Brak messageId");
            String msgId = headerWS.getMessageId();
            webServiceOpLog.setMsgId(msgId);
            responseHeader.setMessageId(msgId);

            for (ProductDTO productDTO:productsRequest.getProducts()){
                String name = productDTO.getName();
                String url = (productDTO.getImageUrl() == null) ? null : productDTO.getImageUrl();
                String description = (productDTO.getDescription() == null) ? null : productDTO.getDescription();
                BigDecimal price = productDTO.getPrice();
                addProductsResponseWS.getProductsId().add(createProduct(new ProductDTO(name, url, description, price)).getId());
            }
            responseHeader.setStatusWS(StatusWS.DONE);
            responseHeader.setIsFailed(false);
            responseHeader.setDateTime(new Date());
            webServiceOpLog.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            responseHeader.setStatusWS(StatusWS.FAILED);
            responseHeader.setErrorMsg(e.getMessage()+"\nBlad podczas dodawania produktów"); //TODO - poprawic
            responseHeader.setIsFailed(true);
            webServiceOpLog.setErrorMsg(e.getMessage());
            webServiceOpLog.setSuccess(false);
        } finally {
            addProductsResponseWS.setHeaderWS(responseHeader);
            logProducer.writeAction(webServiceOpLog);
            return addProductsResponseWS;
        }
    }

    public GetProductsResponseWS getProducts(GetProductsRequestWS requestWS) throws JsonProcessingException {
        GetProductsResponseWS getProductsResponseWS = new GetProductsResponseWS();
        ResponseHeaderWS responseHeader = new ResponseHeaderWS();
        WebServiceOperationLog webServiceOpLog=new WebServiceOperationLog();
        try {
            responseHeader.setSource("SERWER_GLOWNY");
            HeaderWS headerWS = requestWS.getHeaderWS();
            String msgId = (headerWS.getMessageId() == null) ? UUID.randomUUID().toString() : headerWS.getMessageId();
            webServiceOpLog.setMsgId(msgId);
            responseHeader.setMessageId(msgId);
            responseHeader.setDateTime(new Date());
            webServiceOpLog.setDate(new Date());
            Integer limit = requestWS.getProductLimit();
            if (limit==null) throw new NullPointerException("Nieprawidlowa ilosc pobieranych produktow");
            List<ProductDTO> productDTOs = getProducts(limit, null, "desc");
            getProductsResponseWS.setProducts(productDTOs);
            responseHeader.setStatusWS(StatusWS.DONE);
            responseHeader.setIsFailed(false);
            webServiceOpLog.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            responseHeader.setIsFailed(true);
            responseHeader.setStatusWS(StatusWS.FAILED);
            responseHeader.setErrorMsg(e.getMessage()+"\nBlad podczas pobierania produktów"); //TODO - poprawic
            webServiceOpLog.setErrorMsg(e.getMessage());
            webServiceOpLog.setSuccess(false);
        } finally {
            getProductsResponseWS.setHeaderWS(responseHeader);
            logProducer.writeAction(webServiceOpLog);
            return getProductsResponseWS;
        }
    }
}
