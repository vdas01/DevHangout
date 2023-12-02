package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Question;
import com.springboot.stackoverflow.entity.Tag;


public interface QuestionService {
    void saveQuestion(Question newQuestion, Tag newTag);

    Question editQuestion(Integer questionId);

    void updateQuestion(Integer questionId, String editedContent);

    void deleteQuestion(Integer questionId);

    Question findQuestionById(Integer questionId);
}
