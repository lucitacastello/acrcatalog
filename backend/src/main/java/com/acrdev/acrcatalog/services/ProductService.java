package com.acrdev.acrcatalog.services;

import com.acrdev.acrcatalog.dto.ProductDTO;
import com.acrdev.acrcatalog.entities.Product;
import com.acrdev.acrcatalog.repositories.ProductRepository;
import com.acrdev.acrcatalog.services.exceptions.DatabaseException;
import com.acrdev.acrcatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
        Page<Product> list = repository.findAll(pageRequest);
        return list.map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        if(! repository.existsById(id)){
            throw new ResourceNotFoundException("Entity not found");
        }
        Product product = repository.getReferenceById(id);

        return new ProductDTO(product, product.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto){
        Product entity = new Product();

        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto){
        try{
            Product entity = repository.getReferenceById(id);

            entity = repository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException ex){
            throw new ResourceNotFoundException("Id not found: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException ex){
            throw new DatabaseException("Falha de integridade referencial");
        }
    }
}
