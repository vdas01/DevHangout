package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Comment;

public interface CommentService {
    void saveComment(Comment comments, int questionId);
    void saveAnswerComment(Comment comments, int questionId);

    int deleteCommentById(int commentId);
    Comment findById(int commentId);

    void update(Comment comments, int commentId);

    void updateCommentOfAnswer(Comment comments, int commentId);
}
