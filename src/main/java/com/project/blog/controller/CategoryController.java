package com.project.blog.controller;

import com.project.blog.payloads.ApiResponse;
import com.project.blog.payloads.CategoryDTO;
import com.project.blog.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/")
    public ResponseEntity<?> addCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        if (this.categoryService.isCategoryExistsByName(categoryDTO.getCategoryName())){
            return new ResponseEntity<>(new ApiResponse("Category already exists"), HttpStatus.CONFLICT);
        }
        CategoryDTO createCategory = this.categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(createCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Integer categoryId, @RequestBody @Valid CategoryDTO categoryDTO) {
        CategoryDTO updateCategory = this.categoryService.updateCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(updateCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId) {
        this.categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(new ApiResponse("Category Successfully Deleted"), HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Integer categoryId) {
        CategoryDTO getCategory = this.categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(getCategory, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return new ResponseEntity<>(this.categoryService.getAllCategories(), HttpStatus.OK);
    }

}
