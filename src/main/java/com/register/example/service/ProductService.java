package com.register.example.service;

import com.register.example.dto.ProductDTO;
import com.register.example.entity.Product;
import com.register.example.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {
    private static int PAGE_LIMIT = 20;
    private static int FIRST_PAGE = 0;
    private static String NAME="";
    private static String DEFAULT_SORT_BY_NAME = "name";
    private static BigDecimal PRICE_MIN=BigDecimal.ZERO;
    private static BigDecimal PRICE_MAX=BigDecimal.valueOf(Long.MAX_VALUE);


    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> getProducts(Integer size,Integer page,String sort) {
        log.info(String.format("REST - getProducts size= %d page %d sort %s",size,page,sort));
        PageRequest pageRequest = new PageRequest(setPageSize(page), setReturnSize(size), setSortDirection(sort), DEFAULT_SORT_BY_NAME);
        return productRepository.findAll(pageRequest).getContent().stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    public ProductDTO getProduct(final Long productId) {
        System.out.println("ProductService getProduct o id"+productId);
        System.out.println(productRepository.findAll().toString());
        System.out.println(productRepository.findProductById(productId).toString());
        return new ProductDTO(productRepository.findProductById(productId));
    }

    public void deleteProduct(final Long id) {
        productRepository.delete(id);
    }

    public void createProduct(final ProductDTO productDomain) {
        productRepository.save(new Product(
                productDomain.getName(),
                productDomain.getImageUrl(),
                productDomain.getDescription(),
                productDomain.getPrice()));
    }

    public void updateProduct(final Long productId, final ProductDTO productDomain) {
        productRepository.updateProduct(
                productDomain.getImageUrl(),
                productDomain.getDescription(),
                productDomain.getPrice(),
                productId);
    }

    private int setReturnSize(final Integer size) {
        return (Objects.isNull(size) ? PAGE_LIMIT : size.intValue());
    }

    private int setPageSize(final Integer page) {
        return (Objects.isNull(page) ? FIRST_PAGE : page.intValue());
    }


    private BigDecimal setPriceMin(final Integer priceMin){
        return (Objects.isNull(priceMin)? PRICE_MIN:BigDecimal.valueOf(priceMin.longValue()));
    }
    private BigDecimal setPriceMax(final Integer priceMax){
        return (Objects.isNull(priceMax)? PRICE_MAX:BigDecimal.valueOf(priceMax.longValue()));
    }
    private String setName(final String name){
        return (Objects.isNull(name)? NAME:name);
    }

    private Sort.Direction setSortDirection(final String sort) {
        return (StringUtils.isEmpty(sort) ? null : Sort.Direction.fromString(sort));
    }

}
