package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Question;
import com.springboot.stackoverflow.entity.Role;
import com.springboot.stackoverflow.entity.User;
import com.springboot.stackoverflow.repository.RoleRepository;
import com.springboot.stackoverflow.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.roleRepository = roleRepository;
    }

    @Override
    public String processUser(String userName, String email, String password, String confirmPassword) {
        String roleName = "ROLE_user";
        User user = userRepository.findByEmail(email);
        List<Role> roleList = roleRepository.findAll();
        List<String> roleNames = new ArrayList<>();

        if(user != null) return "error";
        if(!confirmPassword.equals(password)) return "errorPassword";

        String encodedPassword = passwordEncoder.encode(password);
        user = new User(userName, email, encodedPassword);

        for(Role tempRole: roleList) {
            roleNames.add(tempRole.getRole());
        }

        if(roleNames.contains(roleName)) {
            for(Role tempRole: roleList) {
                if(tempRole.getRole().equals(roleName)) {
                    user.addRole(tempRole);
                    break;
                }
            }
        }
        else {
            Role theRole = new Role(roleName);
            roleRepository.save(theRole);
            user.addRole(theRole);
        }

        userRepository.save(user);

        return "success";
    }

    @Override
    public User findUserByUserId(Integer userId) {
        if(userId == null)
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByEmail(authentication.getName());

            return userRepository.findUserById(user.getId());
        }
        else {
            return userRepository.findUserById(userId);
        }

    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUserName(String name) {
        return userRepository.findUserByUserName(name);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override

    public List<Question> getBookmarkQuestionsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        return user.getSavedQuestions();
    }

    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName());
    }

    @Transactional
    @Override
    public void saveCommentList(User user) {
        userRepository.save(user);
    }

    @Override
    public void follow(String follower, String following) {
        User followerUser = userRepository.findByEmail(follower);
        User followingUser = userRepository.findByEmail(following);

        followerUser.addFollowing(followingUser);

        userRepository.save(followerUser);
    }

    @Override
    public void unfollow(String follower, String following) {
        User followerUser = userRepository.findByEmail(follower);
        User followingUser = userRepository.findByEmail(following);

        List<User> followings = followerUser.getFollowings();
        followings.remove(followingUser);
        followerUser.setFollowings(followings);

        userRepository.save(followerUser);
    }

    public void updateUser(String userName, String country, String title, String about) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        user.setUserName(userName);
        user.setCountry(country);
        user.setTitle(title);
        user.setAbout(about);

        userRepository.save(user);
    }

    @Override
    public User editUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user=userRepository.findByEmail(authentication.getName());
        return user;
    }


    @Override
    public void saveProfilePic(MultipartFile file,int userId) throws IOException {
        Optional<User> retrievedUser = userRepository.findById(userId);
        if(retrievedUser.isPresent()) {
            User user = retrievedUser.get();
            if (!file.isEmpty()) {
                System.out.println("not empty");
                user.setPhoto(file.getOriginalFilename());
                File file1 = new ClassPathResource("static/css/image").getFile();

                Path path = Paths.get(file1.getAbsolutePath() + File.separator + file.getOriginalFilename());//create a path
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                userRepository.save(user);
            }
            else{
                System.out.println("Empty");
            }
        }
    }


}


