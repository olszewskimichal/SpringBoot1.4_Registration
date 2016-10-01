package com.register.example.repository;

import com.register.example.entity.Product;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Collection<Product> findProductsByName(String name);

    Optional<Product> findProductById(Long id);

    @Cacheable("products")
    Page<Product> findAll(Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Product p set p.imageUrl = ?1, p.description = ?2, p.name = ?3, p.price=?4 where p.id = ?5")
    int updateProduct(String imageUrl, String description, String name, BigDecimal price, Long id);

    @CacheEvict(value = "products", allEntries = true)
    Product save(Product product);
}
