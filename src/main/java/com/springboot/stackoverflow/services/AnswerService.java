package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Answer;

public interface AnswerService {

    void addAnswer(Integer questionId, String content);

    Answer editAnswer(Integer answerId);

    void updateAnswer(String updatedAnswer,Integer answerId);

    void deleteAnswer(Integer answerId);
}
