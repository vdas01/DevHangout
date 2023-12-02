package com.springboot.stackoverflow.controllers;

import com.springboot.stackoverflow.entity.Answer;
import com.springboot.stackoverflow.services.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/editAnswer{answerId}")
    public String processEditAnswer(@PathVariable(name = "answerId")Integer answerId, Model model){
        Answer tempAnswer = answerService.editAnswer(answerId);
        model.addAttribute("answer",tempAnswer);

        //edit answer page
        return "redirect::/";
    }

    @PostMapping("/updateAnswer{answerId}")
    public String processUpdatedAnswer(@ModelAttribute("answer")Answer answer,@PathVariable(name = "answerId")Integer answerId){
            answerService.updateAnswer(answer,answerId);

            //redirect again to that same page
        return "redirect:/";
    }

    @GetMapping("/deleteAnswer{answerId}")
    public String processDeleteAnswer(@PathVariable Integer answerId){
            answerService.deleteAnswer(answerId);

            //redirect to homepage
                return "redirect:/";
    }
}
