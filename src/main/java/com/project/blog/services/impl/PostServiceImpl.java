package com.project.blog.services.impl;

import com.project.blog.entities.Category;
import com.project.blog.entities.Post;
import com.project.blog.entities.User;
import com.project.blog.exceptions.ResourceNotFoundException;
import com.project.blog.payloads.PostDTO;
import com.project.blog.payloads.PostResponse;
import com.project.blog.repositories.CategoryRepository;
import com.project.blog.repositories.PostRepository;
import com.project.blog.repositories.UserRepository;
import com.project.blog.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    private Post PostDTOToPost(PostDTO postDTO) {
        return this.modelMapper.map(postDTO, Post.class);
    }

    private PostDTO PostToPostDTO(Post post) {
        return this.modelMapper.map(post, PostDTO.class);
    }

    @Override
    public PostDTO createPost(PostDTO postDTO, Integer categoryId, Integer userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", String.valueOf(userId)));
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(categoryId)));

        Post post = this.PostDTOToPost(postDTO);
        post.setImageName("default.png");
        post.setAddedDate(new Date());
        post.setCategory(category);
        post.setUser(user);

        Post savedPost = this.postRepository.save(post);
        return this.PostToPostDTO(savedPost);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, Integer id) {
        //User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", String.valueOf(userId)));
        //Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(categoryId)));
        Post post = this.postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", String.valueOf(id)));

        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setImageName(postDTO.getImageName());
        post.setAddedDate(new Date());

        Post update = this.postRepository.save(post);
        return this.PostToPostDTO(update);
    }

    @Override
    public PostDTO getPostById(Integer id) {
        Post post = this.postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", String.valueOf(id)));
        return this.PostToPostDTO(post);
    }

    @Override
    public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        pageNumber -= 1;
        if (pageNumber < 0) {
            pageNumber = 0;
        }
        Sort sort;
        if (sortOrder.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> pagePost = this.postRepository.findAll(p);
        List<Post> allPosts = pagePost.getContent();
        List<PostDTO> postDTOs = allPosts.stream().map(this::PostToPostDTO).toList();

        return new PostResponse(postDTOs, pagePost.getNumber()+1, pagePost.getSize(), pagePost.getTotalElements(), pagePost.getTotalPages(), pagePost.isLast());
    }

    @Override
    public void deletePost(Integer id) {
        Post post = this.postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", String.valueOf(id)));
        this.postRepository.delete(post);
    }

    @Override
    public PostResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        pageNumber -= 1;
        if (pageNumber < 0) {
            pageNumber = 0;
        }
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(categoryId)));

        Sort sort;
        if (sortDir.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> pagePost = this.postRepository.findAllByCategory(category, p);
        List<Post> allPosts = pagePost.getContent();
        //return allByCategory.stream().map(post -> this.PostToPostDTO(post)).collect(Collectors.toList());
        List<PostDTO> postDTOs = allPosts.stream().map(this::PostToPostDTO).toList();

        return new PostResponse(postDTOs, pagePost.getNumber()+1, pagePost.getSize(), pagePost.getTotalElements(), pagePost.getTotalPages(), pagePost.isLast());
    }

    @Override
    public PostResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        pageNumber -= 1;
        if (pageNumber < 0) {
            pageNumber = 0;
        }
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", String.valueOf(userId)));

        Sort sort;
        if (sortDir.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> pagePost = this.postRepository.findAllByUser(user, p);
        List<Post> allPosts = pagePost.getContent();
        List<PostDTO> postDTOs = allPosts.stream().map(this::PostToPostDTO).toList();
        return new PostResponse(postDTOs, pagePost.getNumber()+1, pagePost.getSize(), pagePost.getTotalElements(), pagePost.getTotalPages(), pagePost.isLast());
    }

    @Override
    public List<PostDTO> searchPosts(String keyword) {
        List<Post> byTitleContaining = this.postRepository.findByTitleContaining(keyword);
        return byTitleContaining.stream().map(this::PostToPostDTO).toList();
    }

    @Override
    public Boolean isPostExistByTitle(String title) {
        return this.postRepository.existsByTitle(title);
    }

    @Override
    public Boolean isPostExistsById(Integer id) {
        return this.postRepository.existsById(id);
    }

    @Override
    public void deleteAllPostsByUser(String email) {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        this.postRepository.deleteAll(this.postRepository.findAllByUser(user));
    }

    @Override
    public void deleteAllPostsByCategory(Integer categoryId) {
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(categoryId)));
        this.postRepository.deleteAll(this.postRepository.findAllByCategory(category));
    }

    @Override
    public void deleteAllPosts() {
        this.postRepository.deleteAll();
    }
}
