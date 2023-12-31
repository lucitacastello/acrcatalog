package com.acrdev.acrcatalog.services;

import com.acrdev.acrcatalog.dto.CategoryDTO;
import com.acrdev.acrcatalog.dto.ProductDTO;
import com.acrdev.acrcatalog.entities.Category;
import com.acrdev.acrcatalog.entities.Product;
import com.acrdev.acrcatalog.projections.ProductProjection;
import com.acrdev.acrcatalog.repositories.CategoryRepository;
import com.acrdev.acrcatalog.repositories.ProductRepository;
import com.acrdev.acrcatalog.services.exceptions.DatabaseException;
import com.acrdev.acrcatalog.services.exceptions.ResourceNotFoundException;
import com.acrdev.acrcatalog.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged_(Pageable pageable) {
        Page<Product> list = repository.findAll(pageable);
        return list.map(ProductDTO::new);
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(String name, String categoryId, Pageable pageable) {

        List<Long> categoryIds = Arrays.asList();
        if (!"0".equals(categoryId)) {
            categoryIds = Arrays.stream(categoryId.split(",")).map(Long::parseLong).toList();
        }

        Page<ProductProjection> page = repository.searchProducts(categoryIds, name, pageable);
        List<Long> productIds = page.map(x -> x.getId()).toList();

        //desordenado
        List<Product> entities = repository.searchProductsWithCategories(productIds);
        //método aux para ordenar a list
        //casting para Generics
        entities = (List<Product>) Utils.replace(page.getContent(), entities);
        List<ProductDTO> dtos = entities.stream().map(p -> new ProductDTO(p, p.getCategories())).collect(Collectors.toList());

        Page<ProductDTO> pageDTO = new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());

        return pageDTO;

    }

//    @Transactional(readOnly = true)
//    public Page<ProductProjection> findAllPaged(String name, String categoryId, Pageable pageable) {
//
////        String[] vet = categoryId.split(",");
////        List<String> list = Arrays.asList(vet);
////        List<Long> categoryIds = list.stream().map(x -> Long.parseLong(x)).toList();
//        // ou
//
////        List<Long>categoryIds = Arrays.asList(categoryId.split(",")).stream().map(Long::parseLong).toList();
//        // ou
//        List<Long> categoryIds = Arrays.asList();
//        if (!"0".equals(categoryId)) {
//            categoryIds = Arrays.stream(categoryId.split(",")).map(Long::parseLong).toList();
//        }
//
//        return repository.searchProducts(categoryIds, name, pageable);
//
//    }

//    @Transactional(readOnly = true)
//    public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
//        Page<Product> list = repository.findAll(pageRequest);
//        return list.map(ProductDTO::new);
//    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Entity not found");
        }
        Product product = repository.getReferenceById(id);

        return new ProductDTO(product, product.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        //método auxiliar
        copyDtoToEntity(dto, entity);

        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getReferenceById(id);
            //método auxiliar
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException ex) {
            throw new ResourceNotFoundException("Id not found: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
        entity.setDate(dto.getDate());

        entity.getCategories().clear();
        for (CategoryDTO catDto : dto.getCategories()) {
            Category category = categoryRepository.getReferenceById(catDto.getId());
            entity.getCategories().add(category);
        }

    }

}
