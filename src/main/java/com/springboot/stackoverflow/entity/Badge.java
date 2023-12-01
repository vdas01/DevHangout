package com.springboot.stackoverflow.entity;


import jakarta.persistence.*;

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