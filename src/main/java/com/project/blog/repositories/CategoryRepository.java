package com.project.blog.repositories;

import com.project.blog.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Boolean existsByCategoryName(String name);
}
