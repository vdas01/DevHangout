package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Answer;
import com.springboot.stackoverflow.entity.Comment;
import com.springboot.stackoverflow.entity.Question;
import com.springboot.stackoverflow.entity.User;
import com.springboot.stackoverflow.repository.CommentRepository;
import com.springboot.stackoverflow.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final UserService userService;
    private final AnswerService answerService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, QuestionRepository questionRepository, QuestionService questionService, UserService userService, AnswerService answerService) {
        this.commentRepository = commentRepository;
        this.questionRepository = questionRepository;
        this.questionService = questionService;
        this.userService = userService;
        this.answerService = answerService;
    }

    @Override
    public void saveComment(Comment comments,int questionId) {
        User user=userService.getUser();
        comments.setUserName(user.getUserName());
        comments.setEmail(user.getEmail());
        Question question = questionService.findQuestionById(questionId);
        question.addComment(comments);
        user.addComment(comments);

        questionService.saveCommentList(question);
        userService.saveCommentList(user);

    }

    public void saveAnswerComment(Comment comments,int answerId) {
        User user=userService.getUser();
        comments.setUserName(user.getUserName());
        comments.setEmail(user.getEmail());
        user.addComment(comments);
        userService.saveCommentList(user);

        Answer answer = answerService.findQuestionById(answerId);
        answer.addComment(comments);
        answerService.saveCommentList(answer);

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
        Comment comment = commentRepository.findById(commentId).orElse(null);
        comment.setComment(comments.getComment());
        commentRepository.save(comment);
    }

    @Override
    public void updateCommentOfAnswer(Comment comments, int commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        comment.setComment(comments.getComment());
        commentRepository.save(comments);
    }
}
