package com.springboot.stackoverflow.controllers;

import com.springboot.stackoverflow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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

        if(response.equals("errorPassword"))
            return "redirect:/signup?errorPassword";
        else if(response.equals("error"))
            return "redirect:/signup?error";
        else
            return "redirect:/login?success";
    }
    @GetMapping("/userProfile")
    public String userProfile(){
        return "UserProfile";
    }
}
