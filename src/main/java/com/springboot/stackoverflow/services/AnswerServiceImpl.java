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
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    public void addAnswer(Integer questionId, String content, MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        Optional<Question> questionRetrievedByID = questionRepository.findById(questionId);
        if(questionRetrievedByID.isPresent()){
            Question question = questionRetrievedByID.get();
            Answer answer = new Answer(content, question, null);

            answer.setAuthor(user.getUserName());
            answer.setUser(user);

            if(!file.isEmpty()){
                answer.setPhoto(file.getOriginalFilename());
                File file1 = new ClassPathResource("static/css/image").getFile();

                Path path = Paths.get(file1.getAbsolutePath() + File.separator + file.getOriginalFilename());//create a path
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
            }
            user.setReputation(user.getReputation()+5);
            userRepository.save(user);
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
    public Answer findAnswerById(int answerId) {
        return answerRepository.findById(answerId).orElse(null);
    }

    @Override
    public void saveCommentList(Answer answer) {
        answerRepository.save(answer);
    }
}
