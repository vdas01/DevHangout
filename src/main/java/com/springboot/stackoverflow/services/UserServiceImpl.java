package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.User;
import com.springboot.stackoverflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String processUser(String userName, String email, String password, String confirmPassword) {
        User user = userRepository.findByEmail(email);

        if(user != null) return "error";
        if(!confirmPassword.equals(password)) return "errorPassword";

        user = new User(userName, email, password);
        userRepository.save(user);

        return "success";
    }

    @Override
    public User findUserByUserId(int userId) {
        return userRepository.findUserById(userId);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUserName(String name) {
        return userRepository.findUserByUserName(name);
    }

}
