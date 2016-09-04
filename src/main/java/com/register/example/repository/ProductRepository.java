package com.register.example.repository;

import com.register.example.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
        Collection<Product> findProductByName(String name);

        Product findProductById(Long id);

        Page<Product> findAll(Pageable pageable);

        @Transactional
        @Modifying
        @Query("update Product p set p.imageUrl = ?1, p.description = ?2, p.price = ?3 where p.id = ?4")
        int updateProduct(String imagerUrl, String description, BigDecimal price, Long id);
}