package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Answer;
import com.springboot.stackoverflow.entity.Question;
import com.springboot.stackoverflow.repository.AnswerRepository;
import com.springboot.stackoverflow.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService{

    QuestionRepository questionRepository;
    AnswerRepository answerRepository;

    @Autowired
    public AnswerServiceImpl(QuestionRepository questionRepository,AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public void addAnswer(Integer questionId, String content) {
        Optional<Question> questionRetrievedByID = questionRepository.findById(questionId);
        if(questionRetrievedByID.isPresent()){
            Question question = questionRetrievedByID.get();
            Answer answer = new Answer(content, question, null);
            answer.setAuthor("user");
            answerRepository.save(answer);
        }
    }

    @Override
    public Answer editAnswer(Integer answerId) {
        Optional<Answer> retrievedAnswerById = answerRepository.findById(answerId);
        Answer tempAnswer = null;
        if(retrievedAnswerById.isPresent()){
            tempAnswer = retrievedAnswerById.get();
        }
        return tempAnswer;
    }

    @Override
    public void updateAnswer(String updatedAnswer, Integer answerId) {
        Answer answer = answerRepository.findById(answerId).get();
        answer.setContent(updatedAnswer);
        answerRepository.save(answer);
    }

    @Override
    public void deleteAnswer(Integer answerId) {
            answerRepository.deleteById(answerId);
    }

    @Override
    public Answer findQuestionById(int answerId) {
        return answerRepository.findById(answerId).orElse(null);
    }

    @Override
    public void saveCommentList(Answer answer) {
        answerRepository.save(answer);
    }
}
