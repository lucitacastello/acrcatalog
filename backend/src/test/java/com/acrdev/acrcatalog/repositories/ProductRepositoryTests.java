package com.acrdev.acrcatalog.repositories;

import com.acrdev.acrcatalog.entities.Product;
import com.acrdev.acrcatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    private long idExists;
    private long nonExistingId;
    private long countTotalProduct;


    @BeforeEach
    void setUp() throws Exception {
        idExists = 1L;
        nonExistingId = 1000L;
        countTotalProduct = 25L;
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {

        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProduct + 1, product.getId());
    }

    @Test
    public void findByIdShouldReturnNonEmptyOptionalWhenIdExists() {

        Optional<Product> result = repository.findById(idExists);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExists() {

        Optional<Product> result = repository.findById(nonExistingId);
        //Assertions.assertFalse(result.isPresent());
        Assertions.assertTrue(result.isEmpty());
    }


    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        repository.deleteById(idExists);
        Optional<Product> result = repository.findById(idExists);
        Assertions.assertFalse(result.isPresent());
    }


    // mudança no deleteById da versão 3.X.X do spring boot - ele não lança exception
    //esse teste falhou por causa da versão - Spring boot 2
    //não vai ser implementado
    // para versão 2 do spring boot
//    @Test
//    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
//        long nonExistingId = 100L;
//        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
//            repository.deleteById(nonExistingId);
//        });
//    }


}
