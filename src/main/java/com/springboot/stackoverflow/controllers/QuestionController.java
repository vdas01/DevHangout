package com.springboot.stackoverflow.controllers;

import com.springboot.stackoverflow.entity.Comment;
import com.springboot.stackoverflow.entity.Question;
import com.springboot.stackoverflow.entity.Tag;
import com.springboot.stackoverflow.services.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class QuestionController {

    QuestionService questionService;

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

    @PostMapping("/savequestion")
    public String processSaveQuestion(@ModelAttribute("question") Question newQuestion,@ModelAttribute("tag")Tag newTag){
        questionService.saveQuestion(newQuestion,newTag);
        return "SaveQuestion";
    }

    @GetMapping("/viewQuestion/{questionId}")
    public String viewQuestion(@PathVariable("questionId") Integer questionId, Model model) {
        Question question = questionService.findQuestionById(questionId);
        if(question == null) return "error";
        model.addAttribute("comments",new Comment());
        model.addAttribute("question", question);

        return "questionPage";
    }

    @GetMapping("/editQuestion{questionId}")
    public String processEditQuestion(@PathVariable("questionId")Integer questionId,Model model){
            Question question = questionService.editQuestion(questionId);
            model.addAttribute("question",question);
            //edit question page;
        return "UpdateQuestion";
    }

    @PostMapping("/updateQuestion{questionId}")
    public String processUpdatedQuestion(@PathVariable("questionId") Integer questionId,@ModelAttribute("question") Question question
                ,@ModelAttribute("tag")String updatedTags){
            questionService.updateQuestion(questionId,question,updatedTags);
            //go to updated page of that indidvidual question
        return "redirect:/";
    }

    @GetMapping("/deleteQuestion{questionId}")
    public String deleteQuestion(@PathVariable("questionId")Integer questionId){
        questionService.deleteQuestion(questionId);
        //return to homepage
        return "redirect:/";
    }

    public List<Question> findAllQuestions() {
        return questionService.findQuestionsList();
    }
}
