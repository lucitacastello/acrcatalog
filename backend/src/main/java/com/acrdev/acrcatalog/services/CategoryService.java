package com.acrdev.acrcatalog.services;

import com.acrdev.acrcatalog.dto.CategoryDTO;
import com.acrdev.acrcatalog.entities.Category;
import com.acrdev.acrcatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true) //lock DB
    public List<CategoryDTO> findAll() {
        List<Category> list = repository.findAll();
        return list.stream().map(CategoryDTO::new).collect(Collectors.toList());


        //  return repository.findAll().stream().map(CategoryDTO::new).collect(Collectors.toList());
        // return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
//        List<CategoryDTO> listDto = list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
//        return listDto;

        //iterando manualmente
//        List<CategoryDTO> listDto = new ArrayList<>();
//        for(Category cat : list){
//            listDto.add(new CategoryDTO(cat));
//        }
    }
}
