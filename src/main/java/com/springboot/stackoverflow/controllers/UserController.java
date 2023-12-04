package com.springboot.stackoverflow.controllers;

import com.springboot.stackoverflow.entity.User;
import com.springboot.stackoverflow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String userProfile(Model model, @RequestParam(value = "userId", required = false) Integer userId) {
        User user = userService.findUserByUserId(userId);
        model.addAttribute("user", user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName());
        System.out.println(user.getEmail());
        System.out.println(user.getEmail().equals(authentication.getName()));

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
    public String editProfile(Principal principal, Model model) {
        String name = principal.getName();
        User user = userService.findUserByUserName(name);
        model.addAttribute("user", user);

        return "editProfile";

    }
}