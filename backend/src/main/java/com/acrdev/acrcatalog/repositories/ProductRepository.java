package com.acrdev.acrcatalog.repositories;

import com.acrdev.acrcatalog.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
