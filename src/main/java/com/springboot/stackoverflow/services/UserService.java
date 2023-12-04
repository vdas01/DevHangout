package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.User;

import java.util.List;

public interface UserService {
    String processUser(String userName, String email, String password, String confirmPassword);
    User findUserByUserId(int userId);

    List<User> findAllUsers();

    User findUserByUserName(String name);

}
