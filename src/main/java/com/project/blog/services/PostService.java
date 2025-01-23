package com.project.blog.services;

import com.project.blog.payloads.PostDTO;
import com.project.blog.payloads.PostResponse;

import java.util.List;

public interface PostService {

    PostDTO createPost(PostDTO postDTO, Integer categoryId, Integer userId);
    PostDTO updatePost(PostDTO postDTO, Integer id);
    PostDTO getPostById(Integer id);
    PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    void deletePost(Integer id);
    PostResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PostResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    List<PostDTO> searchPosts(String keyword);
    Boolean isPostExistByTitle(String title);
    Boolean isPostExistsById(Integer id);
    void deleteAllPostsByUser(String email);
    void deleteAllPostsByCategory(Integer categoryId);
    void deleteAllPosts();

}
