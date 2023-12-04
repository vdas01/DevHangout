package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Question;
import com.springboot.stackoverflow.entity.Tag;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TagService {
    List<Tag> findAllTags();

    List<Question> findAllQuestionsByTag(int tagName);
    Page<Tag> customTags(Integer pageNo, Integer pageSize, String sortField, String sortDir, String search);
}
