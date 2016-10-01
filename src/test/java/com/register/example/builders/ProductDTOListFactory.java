package com.register.example.builders;


import com.register.example.dto.ProductDTO;
import com.register.example.entity.Product;
import com.register.example.repository.ProductRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class ProductDTOListFactory {
    private final ProductRepository repository;

    public ProductDTOListFactory(ProductRepository repository) {
        this.repository = repository;
    }

    public TreeMap<Long,ProductDTO> buildNumberOfProductsAndSave(int numberOfProducts) {
        TreeMap<Long,ProductDTO>  result=new TreeMap<>();
        IntStream.range(0, numberOfProducts).forEachOrdered(number -> {
            Product product = new ProductBuilder(String.format("product_%s", number)).withPrice(new BigDecimal("10.00")).build();
            product=repository.save(product);
            result.put(product.getId(),new ProductDTO(product));

        });
        return result;
    }

    public List<ProductDTO> buildNumberOfProductsWithCustomPriceAndSave(int numberOfProducts){
        List<ProductDTO> productList = new ArrayList<>();
        IntStream.range(0, numberOfProducts).forEachOrdered(number -> {

            Product product = new ProductBuilder(String.format("product_%s", number)).withPrice(new BigDecimal((number)*5).setScale(2, RoundingMode.HALF_UP)).build();
            repository.save(product);
            productList.add(new ProductDTO(product));
        });
        return productList;
    }

}
