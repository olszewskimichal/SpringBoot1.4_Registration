package com.register.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.register.example.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@XmlRootElement(name = "ProductDTO")
@XmlType(name = "ProductDTO")
@NoArgsConstructor
public class ProductDTO implements Serializable {
    @JsonProperty("name")
    private String name;
    @JsonProperty("imageUrl")
    private String imageUrl;
    @JsonProperty("description")
    private String description;
    @JsonProperty("price")
    private BigDecimal price;

    public ProductDTO(Product product) {
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
