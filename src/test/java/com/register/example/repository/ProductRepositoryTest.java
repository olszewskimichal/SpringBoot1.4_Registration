package com.register.example.repository;

import com.register.example.builders.ProductBuilder;
import com.register.example.entity.Product;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@Ignore
public class ProductRepositoryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldReturn0resultOfProductsByName() {
        List<Product> result = (List<Product>) this.productRepository.findProductsByName("name");
        assertThat(result).isEmpty();
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void shouldReturn2resultOfProductsByName() {
        this.testEntityManager.persist(new ProductBuilder("name").build());
        this.testEntityManager.persist(new ProductBuilder("name").withPrice(BigDecimal.TEN).build());
        List<Product> result = (List<Product>) this.productRepository.findProductsByName("name");
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void shouldFindProductById() {
        Product product = this.testEntityManager.persist(new ProductBuilder("name").build());
        Product result = this.productRepository.findProductById(product.getId()).get();
        assertThat(product).isEqualToComparingFieldByField(result);
    }

    @Test
    public void shouldReturnExceptionWhenFindProductNotExistingTokenById() {
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("Podany produkt nie istnieje");
        this.testEntityManager.persist(new ProductBuilder("name").build());
        Product product = this.productRepository.findProductById(12345l)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Podany produkt nie istnieje")));
        assertThat(product).isNull();
    }

}