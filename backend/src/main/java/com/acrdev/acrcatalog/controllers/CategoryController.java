package com.acrdev.acrcatalog.controllers;

import com.acrdev.acrcatalog.dto.CategoryDTO;
import com.acrdev.acrcatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    //sem paginação
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll()    {
        List<CategoryDTO> dto = service.findAll();
        return ResponseEntity.ok().body(dto);
    }
// não é paginada
//    @GetMapping
//    public ResponseEntity<Page<CategoryDTO>> findAll(Pageable pageable)    {
//        //PARÂMETROS: page, size, sort
//        Page<CategoryDTO> list = service.findAllPaged(pageable);
//        return ResponseEntity.ok().body(list);
//    }

//    @GetMapping
//    public ResponseEntity<Page<CategoryDTO>> findAll(
//            @RequestParam(value = "page", defaultValue = "0") Integer page,
//            @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
//            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
//            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy)
// {
//        // @RequestParam é opcional
//        // @PathVariable é obrigatório
//
//        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
//        Page<CategoryDTO> list = service.findAllPaged(pageRequest);
//        return ResponseEntity.ok().body(list);
//    }
//
////    @GetMapping
////    public ResponseEntity<List<CategoryDTO>> findAll() {
////
////        List<CategoryDTO> list = service.findAll();
////        return ResponseEntity.ok().body(list);
////        List<Category> list = new ArrayList<>();
////       list.add(new Category(1L, "Books"));
////       list.add(new Category(2L, "Eletronics"));
////    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
        CategoryDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){

        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto){

        dto = service.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
