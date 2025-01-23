package com.project.blog.services;

import com.project.blog.payloads.CommentDTO;

public interface CommentService {

    CommentDTO createComment(CommentDTO commentDTO, Integer postId);
    void deleteComment(Integer commentId);
    CommentDTO updateComment(CommentDTO commentDTO, Integer commentId);

}
