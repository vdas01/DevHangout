package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Role;
import com.springboot.stackoverflow.entity.User;
import com.springboot.stackoverflow.repository.RoleRepository;
import com.springboot.stackoverflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

}
