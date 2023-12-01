package com.springboot.stackoverflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Comment extends JpaRepository<Comment,Integer> {
}
