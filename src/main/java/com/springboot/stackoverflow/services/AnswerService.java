package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Answer;

public interface AnswerService {

    void addAnswer(Integer questionId, Answer answer);

    Answer editAnswer(Integer answerId);

    void updateAnswer(Answer updatedAnswer,Integer answerId);

    void deleteAnswer(Integer answerId);
}
