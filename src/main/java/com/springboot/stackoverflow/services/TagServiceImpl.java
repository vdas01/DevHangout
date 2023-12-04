package com.springboot.stackoverflow.services;

import com.springboot.stackoverflow.entity.Question;
import com.springboot.stackoverflow.entity.Tag;
import com.springboot.stackoverflow.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService{

    TagRepository tagRepository;
    public TagServiceImpl(){}

    @Autowired
    public TagServiceImpl(TagRepository tagRepository){
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public List<Question> findAllQuestionsByTag(int tagId) {
        Optional<Tag> retrievedTag = tagRepository.findById(tagId);
        Tag tempTag = null;
        if(retrievedTag.isPresent()){
            tempTag = retrievedTag.get();
        }
        if(tempTag != null)
         return tempTag.getQuestions();
        return null;
    }

    @Override
    public Page<Tag> customTags(Integer pageNo, Integer pageSize, String sortField, String sortDir, String search) {
            pageNo = pageNo == null ?  1 : pageNo;
            pageSize = pageSize == null ? 4 : pageSize;

            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

           search =  search == "" ? null : search;
           if(search != null && search.equals("null"))
               search = null;



            Page<Tag> page = null;
           page = tagRepository.searchTags(search,sortField,sortDir,pageable);
           return page;
    }
}
