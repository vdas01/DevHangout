package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Question;
import com.springboot.stackoverflow.entity.Tag;
import com.springboot.stackoverflow.repository.QuestionRepository;
import com.springboot.stackoverflow.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService{
   TagService tagService;
   TagRepository tagRepository;

   QuestionRepository questionRepository;

    public QuestionServiceImpl(){}

    @Autowired
    public QuestionServiceImpl(TagService tagService,QuestionRepository questionRepository,TagRepository tagRepository) {
        this.tagService = tagService;
        this.questionRepository = questionRepository;
        this.tagRepository = tagRepository;
    }



    @Override
    public void saveQuestion(Question newQuestion, Tag newTag) {
        newQuestion.setViews(0);
        newQuestion.setVotes(0);
        newQuestion.setAuthor("user");

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
    public void updateQuestion(Integer questionId, Question updatedQuestion,String updatedTags) {
        updatedQuestion.setId(questionId);
        Map<String,Tag> tempTags = new HashMap<>();
        List<Tag> allTags = tagService.findAllTags();

        for(Tag tag:allTags){
            String name = tag.getName();
            tempTags.put(name,tag);
        }

        String[] tagsArray = updatedTags.split(",");

        for(String tempTag: tagsArray){
            if(tempTags.containsKey(tempTag)){
                updatedQuestion.addTags(tempTags.get(tempTag));
            }
            else {
                tempTag = tempTag.trim();
                Tag tag = new Tag(tempTag);
                updatedQuestion.addTags(tag);
            }
        }

       questionRepository.save(updatedQuestion);
    }

    @Override
    public void deleteQuestion(Integer questionId) {
        questionRepository.deleteById(questionId);
    }

    @Override
    public Question findQuestionById(Integer questionId) {
        Optional<Question> byId = questionRepository.findById(questionId);
        if(byId.isEmpty()) return null;

        return byId.get();
    }
}
