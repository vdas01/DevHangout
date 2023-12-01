package com.springboot.stackoverflow.entity;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class Badge{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name="description")
    private String description;
    @ManyToMany
    @JoinTable(name = "user_badge", joinColumns = @JoinColumn(name = "badge_id"),
            inverseJoinColumns =@JoinColumn(name ="user_id"))
    private List<User> userBadge;

    @OneToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},mappedBy = "badge")
    private User user;
    public Badge() {
    }
    public Badge(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}