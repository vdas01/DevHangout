package com.springboot.stackoverflow.services;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
            if(!file.isEmpty()){
                String fileName = file.getOriginalFilename();
                java.io.File tempFile = java.io.File.createTempFile("temp", null);
                file.transferTo(tempFile.toPath()); // transfer data to io.File

                // Accessing serviceAccount file for firebase Authentication
                try (FileInputStream serviceAccount = new FileInputStream("./serviceAccountKey.json")) {
                    //Creates a BlobId and BlobInfo for the file in firebase Storage.
                    //BlobId is unique identifier of Blob object in firebase
                    BlobId blobId = BlobId.of("stack-overflow-clone-857f4.appspot.com", fileName);
                    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

                    //reading credentials from serviceAccount
                    //uploading file content from temporary file to firebase
                    Credentials credentials = GoogleCredentials.fromStream(serviceAccount);
                    Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
                    storage.create(blobInfo, Files.readAllBytes(tempFile.toPath()));

                    String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/drive-db-415a1/o/%s?alt=media";

                    //saving metadata in file entity

                    user.setPhotoName(file.getOriginalFilename());
                    user.setPhotoLink(String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8)));
                    user.setPhotoSize(file.getSize());  // Set the file size
                    user.setPhotoType(file.getContentType());

                    userRepository.save(user);

                } catch (IOException e) {
                    // Handle the exception (log, throw, or return an error response)
                    throw new RuntimeException("Error uploading file to Firebase Storage", e);
                } finally {
                    // Delete the temporary file
                    tempFile.delete();
                }

            }
            else{
                System.out.println("Empty");
            }
        }
    }

    @Override
    public List<User> searchUser(String search) {
        return userRepository.searchUser(search);
    }


}


