package com.springboot.stackoverflow.controllers;

import com.springboot.stackoverflow.entity.User;
import com.springboot.stackoverflow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

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

        if (response.equals("errorPassword"))
            return "redirect:/signup?errorPassword";
        else if (response.equals("error"))
            return "redirect:/signup?error";
        else
            return "redirect:/login?success";
    }

    @GetMapping("/userProfile")
    public String userProfile(Model model) {
        Integer userId = 2;
        User user = userService.findUserByUserId(userId);
        model.addAttribute("user", user);

        return "UserProfile";
    }

    @GetMapping("/users")
    public String showAllUsers() {
        List<User> user = null;
        user = userService.findAllUsers();
        return "Users";

    }

    @GetMapping("/editProfile")
    public String editProfile(Principal principal, Model model) {
        String name = principal.getName();
        User user = userService.findUserByUserName(name);
        model.addAttribute("user", user);
        return "editProfile";

    }
}