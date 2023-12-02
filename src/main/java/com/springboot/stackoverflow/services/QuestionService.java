package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Question;
import com.springboot.stackoverflow.entity.Tag;


public interface QuestionService {
    void saveQuestion(Question newQuestion, Tag newTag);

    Question editQuestion(Integer questionId);

    void deleteQuestion(Integer questionId);
}
