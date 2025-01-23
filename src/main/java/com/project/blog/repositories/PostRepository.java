package com.project.blog.repositories;

import com.project.blog.entities.Category;
import com.project.blog.entities.Post;
import com.project.blog.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Page<Post> findAllByUser(User user, Pageable pageable);
    Page<Post> findAllByCategory(Category category, Pageable pageable);
    List<Post> findAllByUser(User user);
    List<Post> findAllByCategory(Category category);
    Boolean existsByTitle(String title);
    List<Post> findByTitleContaining(String title);

    //In case if the above method not working fine, and throwing exception like InvalidDataAccessApiUsageException and then we pass the variable in controller as "%"+keyword+"%"
    /*@Query("select p from Post p where p.title like :key")
    List<Post> searchByTitle(@Param("key") String title);*/

}
