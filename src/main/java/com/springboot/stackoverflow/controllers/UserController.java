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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
    private UserService userService;

    private QuestionService questionService;

    @Autowired
    public UserController(UserService userService,QuestionService questionService) {
        this.userService = userService;
        this.questionService = questionService;
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/processUser")
    public String processUser(@ModelAttribute("username") String userName,
                              @ModelAttribute("email") String email,
                              @ModelAttribute("password") String password,
                              @ModelAttribute("confirm-password") String confirmPassword) {
        String response = userService.processUser(userName, email, password, confirmPassword);

        if (response.equals("errorPassword"))
            return "redirect:/signup?errorPassword";
        else if (response.equals("error"))
            return "redirect:/signup?error";
        else
            return "redirect:/login?success";
    }

    @GetMapping("/userProfile")
    public String userProfile(Model model, @RequestParam(value = "userId", required = false) Integer userId) {
        User user = userService.findUserByUserId(userId);
        model.addAttribute("user", user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedUser", userService.findByEmail(authentication.getName()));

        return "UserProfile";
    }

    @GetMapping("/users")
    public String showAllUsers(Model model) {
        List<User> users = null;
        users = userService.findAllUsers();
        model.addAttribute("users", users);

        return "Users";

    }

    @GetMapping("/editProfile")
    public String editProfile(Model model) {
        User user=userService.editUser();
        model.addAttribute("user", user);
        return "editProfile";

    }

    @GetMapping("/users/saves")
    public String processBookmarkQuestions(Model model){
        List<Question> bookmarkQuestions = userService.getBookmarkQuestionsByUser();

        model.addAttribute("questions",bookmarkQuestions);
        return "BookmarkQuestion";

    }
    @PostMapping("/updateProfile")
    public String updateProfile( @RequestParam String userName,
                                 @RequestParam String country,@RequestParam String title,
                                 @RequestParam String about){
        userService.updateUser(userName, country, title, about);
        return "redirect:/userProfile";
    }

    @GetMapping("/follow")
    public String follow(@RequestParam("follower") String follower,
                         @RequestParam("following") String following) {
        userService.follow(follower, following);

        return "redirect:/userProfile?userId=" + userService.findByEmail(following).getId();
    }

    @GetMapping("/unfollow")
    public String unfollow(@RequestParam("follower") String follower,
                         @RequestParam("following") String following) {
        userService.unfollow(follower, following);

        return "redirect:/userProfile?userId=" + userService.findByEmail(following).getId();
    }

    @PostMapping("/saveProfilePic{userId}")
    public String saveProfilePic(@RequestParam("imageName") MultipartFile file,
                                 @PathVariable(value = "userId")int userId) throws IOException {
        System.out.println(file.getOriginalFilename());
        System.out.println(userId);
        userService.saveProfilePic(file,userId);
        return "redirect:/editProfile";
    }

}

