package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Comment;

public interface CommentService {
    void saveComment(Comment comments, int questionId);
}
