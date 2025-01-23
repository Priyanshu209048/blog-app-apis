package com.project.blog.controller;

import com.project.blog.config.AppConstants;
import com.project.blog.payloads.ApiResponse;
import com.project.blog.payloads.PostDTO;
import com.project.blog.payloads.PostResponse;
import com.project.blog.services.FileService;
import com.project.blog.services.PostService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final FileService fileService;

    @Autowired
    public PostController(PostService postService, FileService fileService) {
        this.postService = postService;
        this.fileService = fileService;
    }

    @Value("${project.image}")
    private String path;

    @PostMapping("/user/{userId}/category/{categoryId}")
    public ResponseEntity<?> createPost(@RequestBody PostDTO postDTO, @PathVariable Integer categoryId, @PathVariable Integer userId) {
        if (this.postService.isPostExistByTitle(postDTO.getTitle())) {
            return new ResponseEntity<>(new ApiResponse("Title already exists"), HttpStatus.CONFLICT);
        }

        PostDTO createdPost = this.postService.createPost(postDTO, categoryId, userId);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllPostsByUser(@PathVariable Integer userId,
                                               @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                               @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                               @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
                                               @RequestParam(value = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {
        PostResponse postsByUser = this.postService.getPostsByUser(userId, pageNumber, pageSize, sortBy, sortOrder);
        if (postsByUser.getContent().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("This user didn't post anything"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postsByUser, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getAllPostsByCategory(@PathVariable Integer categoryId,
                                                   @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                   @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                   @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
                                                   @RequestParam(value = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {
        PostResponse postsByCategory = this.postService.getPostsByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        if (postsByCategory.getContent().isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("This category doesn't has any post"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postsByCategory, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllPosts(@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                         @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                         @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
                                         @RequestParam(value = "sortOrder", defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {
        PostResponse posts = this.postService.getAllPosts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Integer postId) {
        PostDTO post = this.postService.getPostById(postId);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Integer postId) {
        this.postService.deletePost(postId);
        return new ResponseEntity<>(new ApiResponse("Post deleted successfully"), HttpStatus.OK);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@RequestBody PostDTO postDTO, @PathVariable Integer postId) {
        PostDTO updatedPost = this.postService.updatePost(postDTO, postId);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<?> searchPost(@PathVariable String keyword) {
        List<PostDTO> postDTOS = this.postService.searchPosts(keyword);
        return new ResponseEntity<>(postDTOS, HttpStatus.OK);
    }

    @PostMapping("/{postId}/upload-image")
    public ResponseEntity<?> uploadPostImage(@PathVariable Integer postId, @RequestParam("image") MultipartFile image) {
        PostDTO post = this.postService.getPostById(postId);

        String fileName = null;
        try {
            fileName = this.fileService.uploadFile(path, image);
        } catch (MultipartException | IOException | StringIndexOutOfBoundsException e) {
            return new ResponseEntity<>(new ApiResponse("Error while uploading image file"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        post.setImageName(fileName);
        PostDTO updatePost = this.postService.updatePost(post, postId);

        if (fileName.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Please Upload the correct image file"), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(updatePost, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/images/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> getImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {
        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

}
