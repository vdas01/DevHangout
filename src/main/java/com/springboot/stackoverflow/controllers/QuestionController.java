package com.springboot.stackoverflow.controllers;

import com.springboot.stackoverflow.entity.Question;
import com.springboot.stackoverflow.entity.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class QuestionController {

    @GetMapping("/questions/ask")
    public String askQuestions(Model model){
        Question question = new Question();
        Tag tag = new Tag();
        model.addAttribute("question",question);
        model.addAttribute("tag",tag);
        return "AskQuestion";
    }

    @GetMapping("/saveQuestion")
    public String saveQuestion(@ModelAttribute("question") Question newQuestion,@ModelAttribute("tag")Tag newTag){
        System.out.println(newQuestion.getTitle());
        System.out.println(newTag.getName());
        return "SaveQuestion";
    }
}
