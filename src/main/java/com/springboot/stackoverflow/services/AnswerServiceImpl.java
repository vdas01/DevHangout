package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Answer;
import com.springboot.stackoverflow.entity.Question;
import com.springboot.stackoverflow.entity.User;
import com.springboot.stackoverflow.repository.AnswerRepository;
import com.springboot.stackoverflow.repository.QuestionRepository;
import com.springboot.stackoverflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService{

    QuestionRepository questionRepository;
    AnswerRepository answerRepository;
    UserRepository userRepository;

    @Autowired
    public AnswerServiceImpl(QuestionRepository questionRepository,AnswerRepository answerRepository
                            ,UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addAnswer(Integer questionId, String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        Optional<Question> questionRetrievedByID = questionRepository.findById(questionId);
        if(questionRetrievedByID.isPresent()){
            Question question = questionRetrievedByID.get();
            Answer answer = new Answer(content, question, null);
            answer.setAuthor(user.getUserName());
            answer.setUser(user);
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
}
