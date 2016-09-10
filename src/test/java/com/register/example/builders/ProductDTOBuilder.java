package com.register.example.builders;

import com.register.example.dto.ProductDTO;

import java.math.BigDecimal;

public class ProductDTOBuilder {
    private int numberOfInstances = 1;

    private String name;
    private String imageUrl = "http://localhost/image";
    private String description = "description";
    private BigDecimal price=BigDecimal.ONE;


    public ProductDTOBuilder(String name) {
        this.name = name;
    }

    public ProductDTOBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ProductDTO build() {
        return new ProductDTO(name, imageUrl, description, price);
    }

}
