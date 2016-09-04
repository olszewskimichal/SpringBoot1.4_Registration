package com.register.example.rest;

import com.register.example.asserts.ProductListAssert;
import com.register.example.builders.ProductDTOBuilder;
import com.register.example.builders.ProductDTOListFactory;
import com.register.example.dto.ProductDTO;
import com.register.example.entity.Product;
import com.register.example.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductRestApiTest {

    @Autowired
    public ProductRepository productRepository;
    public String baseURL="/api/products";

    @Autowired
    private TestRestTemplate restTemplate=new TestRestTemplate("administrator","administrator");

    @Before
    public void setup() {
        productRepository.deleteAll();
    }


    @Test
    public void should_get_empty_list_of_products() {
        givenProduct()
                .buildNumberOfProductsAndSave(0);

        List<ProductDTO> products = thenGetProductsFromApi();

        Assertions.assertThat(products).isEmpty();
    }

    @Test
    public void should_get_one_product() {
        TreeMap<Long,ProductDTO> givenProducts = givenProduct()
                .buildNumberOfProductsAndSave(1);

        for (HashMap.Entry<Long, ProductDTO> e : givenProducts.entrySet()) {
            Long id = e.getKey();
            ProductDTO value = e.getValue();

            ProductDTO product = thenGetOneProductFromApi(id);

            System.out.println(product);
            assertThat(value).isEqualToComparingFieldByField(product);
        }

    }

    @Test
    public void should_get_3_products() {
        List<ProductDTO> givenProduct = new ArrayList<>(givenProduct()
                .buildNumberOfProductsAndSave(6).values());

        List<ProductDTO> products = thenGetNumberOfNewestProductsFromApi(3);

        System.out.println(products.toString());
        System.out.println(givenProduct.toString());

        ProductListAssert.assertThat(products)
                .isSuccessful()
                .hasNumberOfItems(3)
                .newestOf(givenProduct);
    }

    @Test
    public void should_create_a_product() {
        //given
        String expectedName="TestName";

        //when
        thenCreateProductByApi(expectedName);

        //then
        List<Product> products= (List<Product>) productRepository.findProductByName(expectedName);
        Assertions.assertThat(products.get(0)).isNotNull();
    }

    @Test
    public void should_update_existing_product() {
        //given
        TreeMap<Long,ProductDTO> givenProducts = givenProduct()
                .buildNumberOfProductsAndSave(1);

        //when


        for (HashMap.Entry<Long, ProductDTO> e : givenProducts.entrySet()) {
            Long id = e.getKey();
            thenUpdateProductByApi(id,BigDecimal.ONE);
            //then
            assertThat(productRepository.findProductById(id))
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("price", BigDecimal.ONE.setScale(2));
        }
    }

    @Test
    public void should_delete_existing_product() {
        //given
        TreeMap<Long,ProductDTO> givenProducts = givenProduct()
                .buildNumberOfProductsAndSave(1);

        //when
        for (HashMap.Entry<Long, ProductDTO> e : givenProducts.entrySet()) {
            Long id = e.getKey();

            System.out.println(id);
            thenDeleteOneProductFromApi(id);
            //then
            assertThat(productRepository.findProductById(id)).isNull();
        }

    }

    private ProductDTOListFactory givenProduct() {
        return new ProductDTOListFactory(productRepository);
    }


    private ProductDTO thenGetOneProductFromApi(Long id) {
        return restTemplate.getForObject(baseURL+"/{id}", ProductDTO.class,id);
    }

    private List<ProductDTO> thenGetProductsFromApi() {
        return Lists.newArrayList(restTemplate.getForObject(baseURL, ProductDTO[].class));
    }

    private void thenCreateProductByApi(String name) {
        restTemplate.put(baseURL, new ProductDTOBuilder(name).build());
    }

    private void thenUpdateProductByApi(Long id,BigDecimal price) {
        restTemplate.put(baseURL+"/"+id, new ProductDTOBuilder("product_0").withPrice(price).build());
    }

    private List<ProductDTO> thenGetNumberOfNewestProductsFromApi(int number) {
        return Lists.newArrayList(restTemplate.getForEntity(baseURL+String.format("?order=desc&limit=%s", number), ProductDTO[].class).getBody());
    }

    private void thenDeleteOneProductFromApi(Long id) {
        restTemplate.delete(baseURL+"/"+id);
    }


}
