package io.github.darlene.surveyplatformbackend.service;


import io.github.darlene.surveyplatformbackend.domain.Survey;
import io.github.darlene.surveyplatformbackend.exceptions.NotFoundException;
import io.github.darlene.surveyplatformbackend.repository.SurveyRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly= true)
public class SurveyService {

    private final SurveyRepository surveyRepository;

    // Get a whole list of survey
    public List<Survey> findAll(){
        return surveyRepository.findAll();
    }

    // Get survey by id
    public Survey getById(Long id){
        return surveyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Survey", id));
    }

    // Method to create survey
    @Transactional
    public Survey create(String name, String description){
        Survey survey = new Survey(); // Survey init
        survey.setName(name);
        survey.setDescription(description);
        return surveyRepository.save(survey);
    }

    // Method to update survey
    @Transactional
    public Survey update(Long id, String name, String description){
        Survey survey = getById(id);
        survey.setName(name);
        survey.setDescription(name);

        return survey;
    }

    // Method to delete
    @Transactional
    public void delete(Long id){
        surveyRepository.delete(getById(id));
    }

}
