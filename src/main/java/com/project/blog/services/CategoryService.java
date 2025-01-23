package com.project.blog.services;

import com.project.blog.payloads.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(CategoryDTO categoryDTO, Integer id);
    void deleteCategory(Integer id);
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(Integer id);
    Boolean isCategoryExistsByName(String name);

}
