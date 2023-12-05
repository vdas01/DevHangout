package com.springboot.stackoverflow.controllers;

import com.springboot.stackoverflow.entity.Question;
import com.springboot.stackoverflow.entity.User;
import com.springboot.stackoverflow.services.QuestionService;
import com.springboot.stackoverflow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {
    private final QuestionController questionController;
    private final UserController userController;

    private final UserService userService;
    private final QuestionService questionService;

    @Autowired
    public HomeController(QuestionController questionController, UserController userController,QuestionService questionService,UserService userService) {
        this.questionController = questionController;
        this.userController = userController;
        this.questionService = questionService;
        this.userService = userService;
    }

    @RequestMapping("/")
    public String showHome(Model model){
        List<Question> questionList =questionController.findAllQuestions();
        model.addAttribute("questionsList",questionList);
        return "home";
    }
}
