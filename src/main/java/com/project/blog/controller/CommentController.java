package com.project.blog.controller;

import com.project.blog.payloads.ApiResponse;
import com.project.blog.payloads.CommentDTO;
import com.project.blog.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/post/{postId}")
    public ResponseEntity<?> addComment(@RequestBody CommentDTO commentDTO, @PathVariable Integer postId) {
        CommentDTO comment = this.commentService.createComment(commentDTO, postId);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer commentId) {
        this.commentService.deleteComment(commentId);
        return new ResponseEntity<>(new ApiResponse("Comment Deleted Successfully!"), HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@RequestBody CommentDTO commentDTO, @PathVariable Integer commentId) {
        CommentDTO commentDTO1 = this.commentService.updateComment(commentDTO, commentId);
        return new ResponseEntity<>(commentDTO1, HttpStatus.OK);
    }

}
