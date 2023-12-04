package com.springboot.stackoverflow.controllers;

import com.springboot.stackoverflow.entity.Question;
import com.springboot.stackoverflow.entity.Tag;
import com.springboot.stackoverflow.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
public class QuestionController {

    QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService){
        this.questionService = questionService;
    }

    @GetMapping("/questions/ask")
    public String processAskQuestions(Model model){
        Question question = new Question();
        Tag tag = new Tag();
        model.addAttribute("question",question);
        model.addAttribute("tag",tag);
        return "AskQuestion";
    }

    @PostMapping("/saveQuestion")
    public String processSaveQuestion(@RequestParam("imageName") MultipartFile file,
                                      @ModelAttribute("question") Question newQuestion,
                                      @ModelAttribute("tag")Tag newTag) throws IOException {


        questionService.saveQuestion(newQuestion,newTag,file);

        return "redirect:/";
    }

    @GetMapping("/viewQuestion/{questionId}")
    public String viewQuestion(@PathVariable("questionId") Integer questionId, Model model) {
        Question question = questionService.findQuestionById(questionId);

        if(question == null) return "error";
        model.addAttribute("question", question);


        return "questionPage";
    }

    @GetMapping("/editQuestion/{questionId}")
    public String processEditQuestion(@PathVariable("questionId")Integer questionId,Model model){
        Question question = questionService.editQuestion(questionId);
        model.addAttribute("question", question);
        model.addAttribute("isEdited", "true");

        return "questionPage";
    }

    @GetMapping("/updateQuestion/{questionId}")
    public String processUpdatedQuestion(@PathVariable("questionId") Integer questionId,
                                         @ModelAttribute("editedContent") String editedContent){
        questionService.updateQuestion(questionId, editedContent);

        return "redirect:/viewQuestion/{questionId}";
    }

    @GetMapping("/deleteQuestion/{questionId}")
    public String deleteQuestion(@PathVariable("questionId")Integer questionId){
        questionService.deleteQuestion(questionId);

        return "redirect:/";
    }

    public List<Question> findAllQuestions() {
        return questionService.findQuestionsList();
    }
}
