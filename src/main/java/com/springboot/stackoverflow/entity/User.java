package com.springboot.stackoverflow.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "username")
    private String userName;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "reputation")
    private Integer reputation;
    @Column(columnDefinition = "TEXT", name = "about")
    private String about;
    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @ManyToMany()
    @JoinTable(
            name = "followers",
            joinColumns = @JoinColumn(name = "follower_id"), // field from current class
            inverseJoinColumns=@JoinColumn(name = "following_id") // field from other class
    )
    private List<User> followings;

    @ManyToMany()
    @JoinTable(
            name = "followers",
            joinColumns = @JoinColumn(name = "following_id"), // field from current class
            inverseJoinColumns=@JoinColumn(name = "follower_id") // field from other class
    )
    private List<User> followers;

    public User() {}

    public User(String userName, String email,
                String password, Integer reputation,
                String about, Date createdAt) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.reputation = reputation;
        this.about = about;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<User> getFollowings() {
        return followings;
    }

    public void setFollowings(List<User> followings) {
        this.followings = followings;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public void addFollowers(User theUser) {
        if(followers == null) {
            followers = new ArrayList<>();
        }

        followers.add(theUser);
    }

    public void addFollowing(User theUser) {
        if(followings == null) {
            followings = new ArrayList<>();
        }

        followings.add(theUser);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", reputation=" + reputation +
                ", about='" + about + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
