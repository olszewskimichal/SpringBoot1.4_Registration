package com.register.example.restControllers;

import com.register.example.dto.ProductDTO;
import com.register.example.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Slf4j
@Profile("!test")
public class ProductsRestController {

    private final ProductService productService;

    @Autowired
    public ProductsRestController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}",
            produces = "application/json"
    )
    public @ResponseBody ProductDTO getProduct(@PathVariable("id") Long productId) {
        System.out.println("restController getProduct o id" + productId);
        return productService.getProduct(productId);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public  @ResponseBody List<ProductDTO> getProducts(
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "order", required = false) String sort
        /*     @RequestParam(value = "name", required = false) String name
           @RequestParam(value = "priceMin",required = false) Integer priceMin,
            @RequestParam(value = "priceMax",required = false) Integer priceMax*/
    ) {
        System.out.println("restController getProducts ");
        // return productService.getProducts(limit, sort,name,priceMin,priceMax);
        return productService.getProducts(limit, page, sort);
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            value = "/{id}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduct(
            @PathVariable("id") Long id,
            @RequestBody ProductDTO productDomain
    ) {
        productService.updateProduct(id, productDomain);
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addProduct(
            @RequestBody ProductDTO productDomain
    ) {
        productService.createProduct(productDomain);
    }

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{id}"
    )
    public void deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
    }

}
