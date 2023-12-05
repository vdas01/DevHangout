package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.*;
import com.springboot.stackoverflow.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService{
   TagService tagService;
   TagRepository tagRepository;
   QuestionRepository questionRepository;
   UserRepository userRepository;
   AnswerRepository answerRepository;
   VoteRepository voteRepository;
    public QuestionServiceImpl(){}

    @Autowired
    public QuestionServiceImpl(TagService tagService,QuestionRepository questionRepository,
                               TagRepository tagRepository, UserRepository userRepository,
                               AnswerRepository answerRepository, VoteRepository voteRepository) {
        this.tagService = tagService;
        this.questionRepository = questionRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
        this.voteRepository = voteRepository;
    }

    @Override
    public void saveQuestion(Question newQuestion, Tag newTag, MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        newQuestion.setViews(0);
        newQuestion.setVotes(0);
        newQuestion.setAuthor(user.getUserName());
        newQuestion.setUser(user);

        Map<String,Tag> tempTags = new HashMap<>();
        List<Tag> allTags = tagService.findAllTags();

        for(Tag tag:allTags){
            String name = tag.getName();
            tempTags.put(name,tag);
        }

        String[] tagsArray = newTag.getName().split(",");
        for(String tempTag: tagsArray){
            if(tempTags.containsKey(tempTag)){
                newQuestion.addTags(tempTags.get(tempTag));
            }
            else {
                tempTag = tempTag.trim();
                Tag tag = new Tag(tempTag);
                newQuestion.addTags(tag);
            }
        }


        if(!file.isEmpty()){
            newQuestion.setPhoto(file.getOriginalFilename());
            File file1 = new ClassPathResource("static/css/image").getFile();

            Path path = Paths.get(file1.getAbsolutePath() + File.separator + file.getOriginalFilename());//create a path
            Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
        }

        questionRepository.save(newQuestion);
    }

    @Override
    public Question editQuestion(Integer questionId) {
        Optional<Question> questionRetrieved = questionRepository.findById(questionId);
        Question tempQuestion = null;
        if(questionRetrieved.isPresent()){
                tempQuestion  = questionRetrieved.get();
        }
        return tempQuestion;
    }


    @Override
    public void updateQuestion(Integer questionId, String editedContent) {
        Question updatedQuestion = questionRepository.findById(questionId).get();
        updatedQuestion.setContent(editedContent);

        questionRepository.save(updatedQuestion);
    }

    @Override
    public void deleteQuestion(Integer questionId) {
        Optional<Question> questionRetrievedById = questionRepository.findById(questionId);
        List<Tag> tags = null;
        if(questionRetrievedById.isPresent()){
            Question question = questionRetrievedById.get();
             tags = question.getTags();
        }
        questionRepository.deleteById(questionId);
        System.out.println(tags);
        if(tags != null){
            for(Tag tempTag : tags){
               if(tempTag.getQuestions().isEmpty()){
                   tagRepository.deleteById(tempTag.getId());
               }
            }
        }
    }

    @Override
    public Question findQuestionById(Integer questionId) {
        Optional<Question> byId = questionRepository.findById(questionId);
        if(byId.isEmpty()) return null;
        byId.get().setViews(byId.get().getViews()+1);
        questionRepository.save(byId.get());

        return byId.get();
    }

    @Override
    public List<Question> findQuestionsList() {
        return questionRepository.findAll();
    }

    public void saveCommentList(Question question) {
        questionRepository.save(question);
    }

    @Override
    public void bookmarkQuestion(int questionId) {
        Optional<Question> retrievedQuestionById = questionRepository.findById(questionId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        if(retrievedQuestionById.isPresent()){
            Question question = retrievedQuestionById.get();
            question.addSavedUser(user);
            questionRepository.save(question);
        }
    }

    @Override
    public void removeBookmarkQuestion(int questionId) {
        Optional<Question> retrievedQuestionById = questionRepository.findById(questionId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        if(retrievedQuestionById.isPresent()){
            Question question = retrievedQuestionById.get();
            question.removedSavedUser(user);
            questionRepository.save(question);
        }
    }

    @Override
    public void acceptAnswer(Integer questionId, Integer answerId) {
        Question question = questionRepository.findById(questionId).get();
        Answer answer = answerRepository.findById(answerId).get();
        if(question.getAcceptedAnswer().equals(answer)) {
            question.setAcceptedAnswer(null);
        }
        else
            question.setAcceptedAnswer(answer);

        questionRepository.save(question);
    }

    @Override
    public void votingSystem(Integer vote, String type, Integer questionId, Integer answerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());

        if (type.equals("question")) {
            if (vote.equals(1)) {
                for (Vote tempVote : user.getUserVote()) {
                    System.out.println("here");
                    if (tempVote.getQuestionId().equals(questionId)) {
                        if (tempVote.getDirection().equals(1)) {
                            return;
                        } else if (tempVote.getDirection().equals(-1)) {
                            Question question = questionRepository.findById(questionId).get();
                            question.setVotes(question.getVotes() + 1);
                            user.getUserVote().remove(tempVote);

                            voteRepository.delete(tempVote);
                            questionRepository.save(question);
                            userRepository.save(user);
                            return;
                        } else {
                            break;
                        }
                    }
                }

                Vote theVote = new Vote(answerId, questionId, vote);
                Question question = questionRepository.findById(questionId).get();
                question.setVotes(question.getVotes() + 1);
                questionRepository.save(question);

                user.addVote(theVote);
                userRepository.save(user);
            } else if (vote.equals(-1)) {
                for (Vote tempVote : user.getUserVote()) {
                    if (tempVote.getQuestionId().equals(questionId)) {
                        if (tempVote.getDirection().equals(-1)) {
                            return;
                        } else if (tempVote.getDirection().equals(1)) {
                            System.out.println("here2");
                            Question question = questionRepository.findById(questionId).get();
                            question.setVotes(question.getVotes() - 1);
                            user.getUserVote().remove(tempVote);

                            voteRepository.delete(tempVote);
                            questionRepository.save(question);
                            userRepository.save(user);
                            return;
                        } else {
                            break;
                        }
                    }
                }

                Vote theVote = new Vote(answerId, questionId, vote);
                Question question = questionRepository.findById(questionId).get();
                question.setVotes(question.getVotes() - 1);
                questionRepository.save(question);

                user.addVote(theVote);
                userRepository.save(user);
            }
        } else if (type.equals("answer")) {
            if (vote.equals(1)) {
                for (Vote tempVote : user.getUserVote()) {
                    if (tempVote.getAnswerId().equals(answerId)) {
                        if (tempVote.getDirection().equals(1)) {
                            return;
                        } else if (tempVote.getDirection().equals(-1)) {
                            Answer answer = answerRepository.findById(answerId).get();
                            answer.setVotes(answer.getVotes() + 1);
                            user.getUserVote().remove(tempVote);

                            voteRepository.delete(tempVote);
                            answerRepository.save(answer);
                            userRepository.save(user);
                            return;
                        } else {
                            break;
                        }
                    }
                }

                Vote theVote = new Vote(answerId, null, vote);
                Answer answer = answerRepository.findById(answerId).get();
                answer.setVotes(answer.getVotes() + 1);
                answerRepository.save(answer);

                user.addVote(theVote);
                userRepository.save(user);
            } else if (vote.equals(-1)) {
                for (Vote tempVote : user.getUserVote()) {
                    if (tempVote.getAnswerId().equals(answerId)) {
                        if (tempVote.getDirection().equals(-1)) {
                            return;
                        } else if (tempVote.getDirection().equals(1)) {
                            Answer answer = answerRepository.findById(answerId).get();
                            answer.setVotes(answer.getVotes() - 1);
                            user.getUserVote().remove(tempVote);

                            voteRepository.delete(tempVote);
                            answerRepository.save(answer);
                            userRepository.save(user);
                            return;
                        } else {
                            break;
                        }
                    }
                }

                Vote theVote = new Vote(answerId, null, vote);
                Answer answer = answerRepository.findById(answerId).get();
                answer.setVotes(answer.getVotes() - 1);
                answerRepository.save(answer);

                user.addVote(theVote);
                userRepository.save(user);
            }
        }
    }
}
