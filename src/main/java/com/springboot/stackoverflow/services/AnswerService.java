package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Answer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AnswerService {

    void addAnswer(Integer questionId, String content, MultipartFile file) throws IOException;

    Answer editAnswer(Integer answerId);

    void updateAnswer(String updatedAnswer,Integer answerId);

    void deleteAnswer(Integer answerId);

    Answer findAnswerById(int questionId);

    void saveCommentList(Answer answer);
}
