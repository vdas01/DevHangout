package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> findAllTags();
}
