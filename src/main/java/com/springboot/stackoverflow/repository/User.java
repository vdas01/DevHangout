package com.springboot.stackoverflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface User extends JpaRepository<User,Integer> {
}
