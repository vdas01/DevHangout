package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.User;

import java.util.List;

public interface UserService {
    String processUser(String userName, String email, String password, String confirmPassword);
    User findUserByUserId(Integer userId);

    List<User> findAllUsers();

    User findUserByUserName(String name);

    User findByEmail(String email);
    void updateUser(String userName, String country, String title, String about);

    User editUser();

    User getUser();

    void saveCommentList(User user);
}


