package com.acrdev.acrcatalog.services;

import com.acrdev.acrcatalog.dto.CategoryDTO;
import com.acrdev.acrcatalog.entities.Category;
import com.acrdev.acrcatalog.repositories.CategoryRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true) //lock DB
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
        Page<Category> list = repository.findAll(pageRequest);
        return list.map(CategoryDTO::new);
    }

//    @Transactional(readOnly = true) //lock DB
//    public List<CategoryDTO> findAll() {
//        List<Category> list = repository.findAll();
//        return list.stream().map(CategoryDTO::new).collect(Collectors.toList());
//
//
//        //  return repository.findAll().stream().map(CategoryDTO::new).collect(Collectors.toList());
//        // return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
////        List<CategoryDTO> listDto = list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
////        return listDto;
//
//        //iterando manualmente
////        List<CategoryDTO> listDto = new ArrayList<>();
////        for(Category cat : list){
////            listDto.add(new CategoryDTO(cat));
////        }
//    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Entity not found");
        }
        Category category = repository.getReferenceById(id);

        return new CategoryDTO(category);

//        //usando optional
//        Optional<Category> obj = repository.findById(id);
//        Category entity = obj.get();
//        return new CategoryDTO(entity);

//        return repository.findById(id).map(CategoryDTO::new)
//                .orElseThrow();
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = repository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try {
            Category entity = repository.getReferenceById(id);
            entity.setName(dto.getName());
            entity = repository.save(entity);
            return new CategoryDTO(entity);
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
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }
}
