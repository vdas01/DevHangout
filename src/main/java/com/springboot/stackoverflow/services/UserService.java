package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Question;
import com.springboot.stackoverflow.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    String processUser(String userName, String email, String password, String confirmPassword);
    User findUserByUserId(Integer userId);

    List<User> findAllUsers();

    User findUserByUserName(String name);

    User findByEmail(String email);
    void updateUser(String userName, String country, String title, String about);


    List<Question> getBookmarkQuestionsByUser();


    User editUser();

    User getUser();

    void saveCommentList(User user);

    void follow(String follower, String following);

    void unfollow(String follower, String following);

    void saveProfilePic(MultipartFile file,int userId) throws IOException;

    List<User> searchUser(String search);
}


