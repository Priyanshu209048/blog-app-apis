package com.project.blog.services.impl;

import com.project.blog.entities.Comment;
import com.project.blog.entities.Post;
import com.project.blog.exceptions.ResourceNotFoundException;
import com.project.blog.payloads.CommentDTO;
import com.project.blog.repositories.CommentRepository;
import com.project.blog.repositories.PostRepository;
import com.project.blog.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository , ModelMapper modelMapper, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
    }

    private Comment commentDTOToComment(CommentDTO commentDTO) {
        return this.modelMapper.map(commentDTO, Comment.class);
    }

    private CommentDTO commentToCommentDTO(Comment comment) {
        return this.modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public CommentDTO createComment(CommentDTO commentDTO, Integer postId) {
        Comment comment = this.commentDTOToComment(commentDTO);
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "postId", String.valueOf(postId)));
        comment.setPost(post);
        Comment save = this.commentRepository.save(comment);
        return this.commentToCommentDTO(save);
    }

    @Override
    public void deleteComment(Integer commentId) {
        Comment comment = this.commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", String.valueOf(commentId)));
        this.commentRepository.delete(comment);
    }

    @Override
    public CommentDTO updateComment(CommentDTO commentDTO, Integer commentId) {
        Comment comment = this.commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", String.valueOf(commentId)));
        comment.setContent(commentDTO.getContent());
        return this.commentToCommentDTO(this.commentRepository.save(comment));
    }

}
