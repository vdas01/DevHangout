package com.springboot.stackoverflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Question extends JpaRepository<Question,Integer> {
}
