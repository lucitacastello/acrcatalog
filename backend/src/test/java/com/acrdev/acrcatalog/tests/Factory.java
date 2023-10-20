package com.acrdev.acrcatalog.tests;

import com.acrdev.acrcatalog.dto.ProductDTO;
import com.acrdev.acrcatalog.entities.Category;
import com.acrdev.acrcatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){
        Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2023-10-20T03:00:00Z"));
       // product.getCategories().add(new Category(2L, "Eletronics"));
        product.getCategories().add(createCategory());
        return product;
    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

    public static Category createCategory(){
        return new Category(2L, "Eletronics");
    }
}
