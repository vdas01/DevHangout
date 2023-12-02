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

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, QuestionRepository questionRepository) {
        this.commentRepository = commentRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public void saveComment(Comment comments, int questionId) {
        Question question = questionRepository.findById(questionId).orElse(null);
        comments.setUserName("Tarun");
        comments.setEmail("tarun@mail.com");
        comments.setQuestion(question);
        commentRepository.save(comments);
    }
}
