package com.springboot.stackoverflow.controllers;

import com.springboot.stackoverflow.entity.Question;
import com.springboot.stackoverflow.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final QuestionController questionController;
    private final UserController userController;

    @Autowired
    public HomeController(QuestionController questionController, UserController userController) {
        this.questionController = questionController;
        this.userController = userController;
    }

    @GetMapping("/")
    public String showHome(Model model){
//        List<Question> questionList =questionController.findAllQuestions();
//        model.addAttribute("questionsList",questionList);
        return "home";
    }
}
