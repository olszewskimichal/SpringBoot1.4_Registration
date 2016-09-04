package com.register.example.asserts;

import com.register.example.dto.ProductDTO;
import org.assertj.core.api.ListAssert;

import java.util.Collections;
import java.util.List;

public class ProductListAssert extends ListAssert<ProductDTO> {
    private List<ProductDTO> actual;

    protected ProductListAssert(List<ProductDTO> productList) {
        super(productList);
        this.actual = productList;
    }

    public static ProductListAssert assertThat(List<ProductDTO> actual) {
        return new ProductListAssert(actual);
    }

    public ProductListAssert isSuccessful() {
        assertThat(actual).isNotNull();
        return this;
    }

    public ProductListAssert hasNumberOfItems(int number) {
        assertThat(actual).hasSize(number);
        return this;
    }

    public ProductListAssert newestOf(List<ProductDTO> productList) {
        List<ProductDTO> newestProducts = productList.subList(productList.size() - actual.size(), productList.size());
        Collections.reverse(newestProducts);
        assertThat(actual).usingFieldByFieldElementComparator().containsExactly(newestProducts.toArray(new ProductDTO[newestProducts.size()]));
        return this;
    }

/*
    public void hasPriceBetween(BigDecimal priceMin, BigDecimal priceMax) {
        assertThat(actual).extracting("price").contains(new BigDecimal("0.00"), new BigDecimal("5.00"),new BigDecimal("10.00"));
    }

    public void withNameContains(String name, List<ProductDomain> givenProduct) {
        List<ProductDomain> correctProduct = givenProduct.stream()
                .filter(product -> product.getName().toLowerCase().contains(name))
                .collect(Collectors.toList());
        assertThat(actual).usingFieldByFieldElementComparator().containsExactly(correctProduct.toArray(new ProductDomain[correctProduct.size()]));
    }
    public void withExpectedName(String expectedName){
        assertThat(actual).extracting("name").containsExactly(expectedName);
    }
    */
}
