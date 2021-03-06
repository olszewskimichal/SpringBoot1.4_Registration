package com.register.example.builders;


import com.register.example.entity.Product;

import java.math.BigDecimal;

public class ProductBuilder {
    private int numberOfInstances = 1;

    private String name;
    private String imageUrl = "http://localhost/image";
    private String description = "description";
    private BigDecimal price = BigDecimal.ONE;


    public ProductBuilder(String name) {
        this.name = name;
    }

    public ProductBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Product build() {
        return new Product(name, imageUrl, description, price);
    }

}
