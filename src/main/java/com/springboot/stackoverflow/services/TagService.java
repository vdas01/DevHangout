package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Tag;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TagService {
    List<Tag> findAllTags();

    Page<Tag> customTags(Integer pageNo, Integer pageSize, String sortField, String sortDir, String search);
}
