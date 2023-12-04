package com.springboot.stackoverflow.repository;

import com.springboot.stackoverflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    public User findByEmail(String email);
    public User findUserById(int id);
    User findUserByUserName(String name);

}
