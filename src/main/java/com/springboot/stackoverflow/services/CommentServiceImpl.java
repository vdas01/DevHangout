package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Comment;
import com.springboot.stackoverflow.entity.Question;
import com.springboot.stackoverflow.repository.CommentRepository;
import com.springboot.stackoverflow.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, QuestionRepository questionRepository, QuestionService questionService) {
        this.commentRepository = commentRepository;
        this.questionRepository = questionRepository;
        this.questionService = questionService;
    }

    @Override
    public void saveComment(Comment comments, int questionId) {
        comments.setUserName("Tarun");
        comments.setEmail("tarun@mail.com");
        commentRepository.save(comments);

    }
}
