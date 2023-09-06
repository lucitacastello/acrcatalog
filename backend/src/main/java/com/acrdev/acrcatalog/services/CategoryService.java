package com.acrdev.acrcatalog.services;

import com.acrdev.acrcatalog.entities.Category;
import com.acrdev.acrcatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public List<Category> findAll (){
        List<Category> list = repository.findAll();

        return list;
    }
}
