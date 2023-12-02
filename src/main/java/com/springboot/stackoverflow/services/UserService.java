package com.springboot.stackoverflow.services;

public interface UserService {
    String processUser(String userName, String email, String password, String confirmPassword);
}
