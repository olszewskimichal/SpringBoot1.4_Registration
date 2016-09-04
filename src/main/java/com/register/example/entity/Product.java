package com.register.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String imageUrl;
    private String description;
    private BigDecimal price;

    public Product(String name,String imageUrl,String description,BigDecimal price){
        this.name=name;
        this.imageUrl=imageUrl;
        this.description=description;
        this.price=price;
    }
}
