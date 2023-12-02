package com.springboot.stackoverflow.controllers;

import com.springboot.stackoverflow.entity.Answer;
import com.springboot.stackoverflow.services.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AnswerController {

    AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping("/addAnswer{questionId}")
    public String processAddAnswer(@PathVariable(name = "questionId")Integer questionId,
            @ModelAttribute("answer") Answer answer){
        answerService.addAnswer(questionId,answer);
        //add question link
        return "redirect:/";
    }
}
