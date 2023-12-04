package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Answer;
import com.springboot.stackoverflow.entity.Comment;
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

    @Override
    public int deleteCommentById(int commentId) {
        Comment comment=commentRepository.findById(commentId).orElse(null);
        commentRepository.deleteById(commentId);
        if(comment.getQuestion()!=null) {
            return comment.getQuestion().getId();
        }else{
            Answer answer =comment.getAnswer();
           int questionId = answer.getQuestion().getId();
           return questionId;
        }
    }

    @Override
    public Comment findById(int commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    @Override
    public void update(Comment comments, int commentId) {
        comments.setId(commentId);
        Comment comment = commentRepository.findById(commentId).orElse(null);
        comments.setQuestion(comment.getQuestion());
        commentRepository.save(comments);
    }

    @Override
    public void updateCommentOfAnswer(Comment comments, int commentId) {
        comments.setId(commentId);
        Comment comment = commentRepository.findById(commentId).orElse(null);
        comments.setAnswer(comment.getAnswer());
        commentRepository.save(comments);
    }
}
