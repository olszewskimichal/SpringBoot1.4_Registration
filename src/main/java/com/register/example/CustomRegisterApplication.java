package com.register.example;

import com.register.example.configuration.HibernateStatisticsInterceptor;
import com.register.example.properties.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.config.annotation.EnableWs;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableJms
@EnableWs
@EnableSwagger2
@EnableConfigurationProperties(StorageProperties.class)
public class CustomRegisterApplication {

    public static void main(String... args) {
        SpringApplication.run(CustomRegisterApplication.class, args);
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder factory, DataSource dataSource,
            JpaProperties properties) {
        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.putAll(properties.getHibernateProperties(dataSource));
        jpaProperties.put("hibernate.ejb.interceptor", hibernateInterceptor());
        return factory.dataSource(dataSource).packages("com.register")
                .properties(jpaProperties).build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HibernateJpaSessionFactoryBean sessionFactory() {
        return new HibernateJpaSessionFactoryBean();
    }

    @Bean
    public HibernateStatisticsInterceptor hibernateInterceptor() {
        return new HibernateStatisticsInterceptor();
    }


}
