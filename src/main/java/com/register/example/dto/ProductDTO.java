package com.register.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.register.example.entity.Product;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductDTO implements Serializable{
    private final String name;
    private final String imageUrl;
    private final String description;
    private final BigDecimal price;

    public ProductDTO(Product product){
        this.name = product.getName();
        this.imageUrl = product.getImageUrl();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }

    public ProductDTO(
            @JsonProperty("name") String name,
            @JsonProperty("imageUrl") String imageUrl,
            @JsonProperty("description") String description,
            @JsonProperty("price") BigDecimal price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
    }


}
