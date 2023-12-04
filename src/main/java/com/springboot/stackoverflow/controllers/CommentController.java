package com.springboot.stackoverflow.controllers;

import com.springboot.stackoverflow.entity.Comment;
import com.springboot.stackoverflow.entity.Question;
import com.springboot.stackoverflow.services.CommentService;
import com.springboot.stackoverflow.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommentController {

    private final CommentService commentService;
    private final QuestionService questionService;

    @Autowired
    public CommentController(CommentService commentService, QuestionService questionService) {
        this.commentService = commentService;
        this.questionService = questionService;
    }

    @GetMapping("/addComment/{questionId}")
    public String addComment(@PathVariable("questionId") int questionId, Model model){
        Question question = questionService.findQuestionById(questionId);
        model.addAttribute("comments",new Comment());
        model.addAttribute("addComment","true");
        if(question == null) return "error";
        model.addAttribute("question", question);
        return "questionPage";
    }

    @PostMapping("/saveComment")
    public String saveComment(@ModelAttribute("comments") Comment comments,@RequestParam("questionId") int questionId){
        Question question = questionService.findQuestionById(questionId);
        question.getComments().add(comments);
        commentService.saveComment(comments,questionId);
        questionService.saveCommentList(question);
        System.out.println(question.getComments());
        return "redirect:/viewQuestion/"+questionId;
    }
}
