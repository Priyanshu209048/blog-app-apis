package com.project.blog.services.impl;

import com.project.blog.entities.Category;
import com.project.blog.exceptions.ResourceNotFoundException;
import com.project.blog.payloads.CategoryDTO;
import com.project.blog.repositories.CategoryRepository;
import com.project.blog.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    private Category CategoryDTOToCategory(CategoryDTO categoryDTO) {
        return this.modelMapper.map(categoryDTO, Category.class);
    }

    private CategoryDTO CategoryToCategoryDTO(Category category) {
        return this.modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = this.CategoryDTOToCategory(categoryDTO);
        Category savedCategory = this.categoryRepository.save(category);
        return this.CategoryToCategoryDTO(savedCategory);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Integer id) {
        Category category = this.categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(id)));

        category.setCategoryName(categoryDTO.getCategoryName());
        category.setCategoryDesc(categoryDTO.getCategoryDesc());

        Category savedCategory = this.categoryRepository.save(category);
        return this.CategoryToCategoryDTO(savedCategory);
    }

    @Override
    public void deleteCategory(Integer id) {
        Category category = this.categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(id)));
        this.categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = this.categoryRepository.findAll();
        return categories.stream().map(this::CategoryToCategoryDTO).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(Integer id) {
        Category category = this.categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(id)));
        return this.CategoryToCategoryDTO(category);
    }

    @Override
    public Boolean isCategoryExistsByName(String name) {
        return this.categoryRepository.existsByCategoryName(name);
    }
}
