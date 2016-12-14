package com.register.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.register.example.IntegrationTestBase;
import com.register.example.builders.ProductBuilder;
import com.register.example.builders.ProductDTOBuilder;
import com.register.example.dto.ProductDTO;
import com.register.example.entity.Product;
import com.register.example.jms.OperationLogProducer;
import com.register.example.repository.ProductRepository;
import com.register.example.soap.objects.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class ProductServiceTest extends IntegrationTestBase {

    @Autowired
    ProductRepository productRepository;

    @Mock
    OperationLogProducer logProducer;

    private ProductService productService;


    @Before
    public void setUp() throws Exception {
        productRepository.deleteAll();
        productRepository.save(new ProductBuilder("produkt1").build());
        productRepository.save(new ProductBuilder("produkt2").build());
        productRepository.save(new ProductBuilder("produkt3").build());
        productService = new ProductService(productRepository, logProducer);
    }

    @Test
    public void shouldReturn2ProductsDTODesc() {
        //given
        Integer size = 2;
        Integer page = null;
        String sort = "desc";
        List<ProductDTO> expectedProduct = Arrays.asList(new ProductDTOBuilder("produkt3").build(),
                new ProductDTOBuilder("produkt2").build());

        List<ProductDTO> productDTOList = productService.getProducts(size, page, sort);

        assertThat(productDTOList.get(0).getName()).isEqualTo(expectedProduct.get(0).getName());
        assertThat(productDTOList.get(0).getImageUrl()).isEqualTo(expectedProduct.get(0).getImageUrl());
        assertThat(productDTOList.get(0).getPrice().stripTrailingZeros()).isEqualTo(
                expectedProduct.get(0).getPrice().stripTrailingZeros());
        assertThat(productDTOList.get(0).getDescription()).isEqualTo(expectedProduct.get(0).getDescription());

        assertThat(productDTOList.get(1).getName()).isEqualTo(expectedProduct.get(1).getName());
        assertThat(productDTOList.get(1).getImageUrl()).isEqualTo(expectedProduct.get(1).getImageUrl());
        assertThat(productDTOList.get(1).getPrice().stripTrailingZeros()).isEqualTo(
                expectedProduct.get(1).getPrice().stripTrailingZeros());
        assertThat(productDTOList.get(1).getDescription()).isEqualTo(expectedProduct.get(1).getDescription());
    }

    @Test
    public void shouldReturn3ProductsDTOWithDefaultSettings() {
        //given
        List<ProductDTO> expectedProduct = Arrays.asList(new ProductDTOBuilder("produkt1").build(),
                new ProductDTOBuilder("produkt2").build(), new ProductDTOBuilder("produkt3").build());

        List<ProductDTO> productDTOList = productService.getProducts(null, null, null);

        assertThat(productDTOList.get(0).getName()).isEqualTo(expectedProduct.get(0).getName());
        assertThat(productDTOList.get(0).getImageUrl()).isEqualTo(expectedProduct.get(0).getImageUrl());
        assertThat(productDTOList.get(0).getPrice().stripTrailingZeros()).isEqualTo(
                expectedProduct.get(0).getPrice().stripTrailingZeros());
        assertThat(productDTOList.get(0).getDescription()).isEqualTo(expectedProduct.get(0).getDescription());
        assertThat(productDTOList.size()).isEqualTo(3);
    }

    @Test
    public void shouldReturn2ProductsDesc() {
        //given
        Integer size = 2;
        Integer page = null;
        String sort = "desc";
        List<Product> expectedProduct = Arrays.asList(new ProductBuilder("produkt3").build(),
                new ProductBuilder("produkt2").build());

        List<Product> products = productService.getProductsFromRepo(size, page, sort);

        assertThat(products.get(0).getName()).isEqualTo(expectedProduct.get(0).getName());
        assertThat(products.get(0).getImageUrl()).isEqualTo(expectedProduct.get(0).getImageUrl());
        assertThat(products.get(0).getPrice().stripTrailingZeros()).isEqualTo(
                expectedProduct.get(0).getPrice().stripTrailingZeros());
        assertThat(products.get(0).getDescription()).isEqualTo(expectedProduct.get(0).getDescription());

        assertThat(products.get(1).getName()).isEqualTo(expectedProduct.get(1).getName());
        assertThat(products.get(1).getImageUrl()).isEqualTo(expectedProduct.get(1).getImageUrl());
        assertThat(products.get(1).getPrice().stripTrailingZeros()).isEqualTo(
                expectedProduct.get(1).getPrice().stripTrailingZeros());
        assertThat(products.get(1).getDescription()).isEqualTo(expectedProduct.get(1).getDescription());
    }

    @Test
    public void shouldReturn3ProductsWithDefaultSettings() {
        //given
        List<Product> expectedProduct = Arrays.asList(new ProductBuilder("produkt1").build(),
                new ProductBuilder("produkt2").build(), new ProductBuilder("produkt2").build());

        List<Product> productList = productService.getProductsFromRepo(null, null, null);

        assertThat(productList.get(0).getName()).isEqualTo(expectedProduct.get(0).getName());
        assertThat(productList.get(0).getImageUrl()).isEqualTo(expectedProduct.get(0).getImageUrl());
        assertThat(productList.get(0).getPrice().stripTrailingZeros()).isEqualTo(
                expectedProduct.get(0).getPrice().stripTrailingZeros());
        assertThat(productList.get(0).getDescription()).isEqualTo(expectedProduct.get(0).getDescription());
        assertThat(productList.size()).isEqualTo(3);
    }

    @Test
    public void shouldCreateAndGetProductById() {
        //given
        ProductDTO expected = new ProductDTOBuilder("test").build();
        Product expectedProduct = productRepository.save(productService.createProduct(expected));

        //when
        ProductDTO product = productService.getProduct(expectedProduct.getId());

        //then
        assertThat(expected).isEqualToIgnoringGivenFields(product, "price");
        assertThat(expected.getPrice().stripTrailingZeros()).isEqualTo(product.getPrice().stripTrailingZeros());
    }

    @Test
    public void shouldUpdateProduct() {
        //given
        Product createdProduct = productRepository.save(
                productService.createProduct(new ProductDTOBuilder("test").build()));

        //when
        productService.updateProduct(createdProduct.getId(),
                new ProductDTOBuilder("testNowy").withPrice(BigDecimal.TEN).build());

        //then
        //TODO obsluzyc orElse
        Product product = productRepository.findProductById(createdProduct.getId()).get();
        assertThat(product.getPrice().stripTrailingZeros()).isEqualTo(BigDecimal.TEN.stripTrailingZeros());
        assertThat(product.getName()).isEqualTo("testNowy");
    }

    @Test
    public void shouldAddProductWithAddProductsRequestWS() throws JsonProcessingException {
        AddProductsRequestWS addProductsRequestWS = new AddProductsRequestWS();
        RequestHeaderWS requestHeaderWS = new RequestHeaderWS();
        requestHeaderWS.setMessageId(UUID.randomUUID().toString());
        requestHeaderWS.setDateTime(new Date());
        requestHeaderWS.setSource("ZrodloTestowe");
        addProductsRequestWS.setHeaderWS(requestHeaderWS);
        addProductsRequestWS.setProducts(
                Arrays.asList(new ProductDTOBuilder("produkt1").build(), new ProductDTOBuilder("produkt2").build(),
                        new ProductDTOBuilder("produkt3").build()));

        AddProductsResponseWS addProductsResponseWS = productService.addProducts(addProductsRequestWS);
        ResponseHeaderWS responseHeaderWS = addProductsResponseWS.getHeaderWS();
        assertThat(responseHeaderWS).isNotNull();
        System.out.println(responseHeaderWS);
        assertThat(addProductsResponseWS.getProductsId().size()).isEqualTo(3);
        assertThat(responseHeaderWS.getIsFailed()).isFalse();
        assertThat(responseHeaderWS.getStatusWS()).isEqualTo(StatusWS.DONE);
    }

    @Test
    public void shouldFailedAddProductWithAddProductsRequestWS_with_emptyMsgId() throws JsonProcessingException {
        AddProductsRequestWS addProductsRequestWS = new AddProductsRequestWS();
        RequestHeaderWS requestHeaderWS = new RequestHeaderWS();
        requestHeaderWS.setSource("ZrodloTestowe");
        addProductsRequestWS.setHeaderWS(requestHeaderWS);
        addProductsRequestWS.setProducts(
                Arrays.asList(new ProductDTOBuilder("produkt1").build(), new ProductDTOBuilder("produkt2").build(),
                        new ProductDTOBuilder("produkt3").build()));

        AddProductsResponseWS addProductsResponseWS = productService.addProducts(addProductsRequestWS);
        ResponseHeaderWS responseHeaderWS = addProductsResponseWS.getHeaderWS();
        assertThat(responseHeaderWS.getIsFailed()).isTrue();
        assertThat(responseHeaderWS.getErrorMsg()).isNotEmpty();
        assertThat(responseHeaderWS.getErrorMsg()).isNotNull();
        assertThat(responseHeaderWS.getStatusWS()).isEqualTo(StatusWS.FAILED);
    }

    @Test
    public void shouldGetProductsWithCorectRequest() throws JsonProcessingException {
        GetProductsRequestWS getProductsRequestWS = new GetProductsRequestWS();
        RequestHeaderWS requestHeaderWS = new RequestHeaderWS();
        requestHeaderWS.setMessageId(UUID.randomUUID().toString());
        requestHeaderWS.setDateTime(new Date());
        requestHeaderWS.setSource("ZrodloTestowe");
        getProductsRequestWS.setHeaderWS(requestHeaderWS);
        getProductsRequestWS.setProductLimit(3);

        GetProductsResponseWS getProductsResponseWS = productService.getProducts(getProductsRequestWS);
        ResponseHeaderWS responseHeaderWS = getProductsResponseWS.getHeaderWS();
        assertThat(responseHeaderWS).isNotNull();
        System.out.println(responseHeaderWS);
        assertThat(getProductsResponseWS.getProducts().size()).isEqualTo(3);
        assertThat(responseHeaderWS.getIsFailed()).isFalse();
        assertThat(responseHeaderWS.getStatusWS()).isEqualTo(StatusWS.DONE);
    }

    @Test
    public void shouldFailedGetProductsWithInCorectRequest() throws JsonProcessingException {
        GetProductsRequestWS getProductsRequestWS = new GetProductsRequestWS();
        RequestHeaderWS requestHeaderWS = new RequestHeaderWS();
        requestHeaderWS.setMessageId(UUID.randomUUID().toString());
        requestHeaderWS.setDateTime(new Date());
        requestHeaderWS.setSource("ZrodloTestowe");
        getProductsRequestWS.setHeaderWS(requestHeaderWS);


        GetProductsResponseWS getProductsResponseWS = productService.getProducts(getProductsRequestWS);
        ResponseHeaderWS responseHeaderWS = getProductsResponseWS.getHeaderWS();
        assertThat(responseHeaderWS.getIsFailed()).isTrue();
        assertThat(responseHeaderWS.getErrorMsg()).isNotEmpty();
        assertThat(responseHeaderWS.getErrorMsg()).isNotNull();
        assertThat(responseHeaderWS.getStatusWS()).isEqualTo(StatusWS.FAILED);
    }


}