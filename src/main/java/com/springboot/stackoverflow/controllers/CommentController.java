package com.springboot.stackoverflow.controllers;

import com.springboot.stackoverflow.entity.Comment;
import com.springboot.stackoverflow.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/saveComment")
    public String saveComment(@ModelAttribute("comments") Comment comments,@RequestParam("questionId") int questionId){

        commentService.saveComment(comments, questionId);
        return "redirect:/viewQuestion/{questionId}";
    }
}
