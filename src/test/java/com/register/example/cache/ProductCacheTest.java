package com.register.example.cache;

import com.register.example.IntegrationTestBase;
import com.register.example.builders.ProductBuilder;
import com.register.example.configuration.HibernateStatisticsInterceptor;
import com.register.example.repository.ProductRepository;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;


public class ProductCacheTest extends IntegrationTestBase {

    @Autowired
    private HibernateStatisticsInterceptor hibernateStatisticsInterceptor;

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void setUp() throws Exception {
        productRepository.deleteAll();
        productRepository.save(new ProductBuilder("aaa").build());
        productRepository.save(new ProductBuilder("bbb").build());
        productRepository.save(new ProductBuilder("ccc").build());
        productRepository.save(new ProductBuilder("ddd").build());
        hibernateStatisticsInterceptor.startCounter();

    }

    @After
    public void tearDown() {
        hibernateStatisticsInterceptor.clearCounter();
    }

    @org.junit.Test
    public void should_cache_findAllProduct() {
        productRepository.findAll(new PageRequest(1,4));
        Long queryCount = hibernateStatisticsInterceptor.getQueryCount();
        assertThat(queryCount).isEqualTo(1L);
        hibernateStatisticsInterceptor.clearCounter();

        hibernateStatisticsInterceptor.startCounter();
        productRepository.findAll(new PageRequest(1,4));
        queryCount = hibernateStatisticsInterceptor.getQueryCount();
        assertThat(queryCount).isEqualTo(0L);
    }

    @org.junit.Test
    public void should_cache_afterSaveProduct() {
        productRepository.findAll(new PageRequest(1,4));
        Long queryCount = hibernateStatisticsInterceptor.getQueryCount();
        assertThat(queryCount).isEqualTo(1L);
        hibernateStatisticsInterceptor.clearCounter();

        hibernateStatisticsInterceptor.startCounter();
        productRepository.findAll(new PageRequest(1,4));
        queryCount = hibernateStatisticsInterceptor.getQueryCount();
        assertThat(queryCount).isEqualTo(0L);
        hibernateStatisticsInterceptor.clearCounter();

        System.out.println();
        System.out.println();
        hibernateStatisticsInterceptor.startCounter();
        productRepository.save(productRepository.save(new ProductBuilder("aaa").build()));
        productRepository.findAll(new PageRequest(1,4));
        queryCount = hibernateStatisticsInterceptor.getQueryCount();
        assertThat(queryCount).isGreaterThan(1L);
    }

}
