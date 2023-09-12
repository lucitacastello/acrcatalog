package com.acrdev.acrcatalog.controllers;

import com.acrdev.acrcatalog.dto.CategoryDTO;
import com.acrdev.acrcatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {

        List<CategoryDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
//        List<Category> list = new ArrayList<>();
//        list.add(new Category(1L, "Books"));
//        list.add(new Category(2L, "Eletronics"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
        CategoryDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }
}
