package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Answer;

public interface AnswerService {

    void addAnswer(Integer questionId, Answer answer);
}
