package io.github.darlene.surveyplatformbackend.survey.service;

import io.github.darlene.surveyplatformbackend.shared.exception.ResourceNotFoundException;
import io.github.darlene.surveyplatformbackend.survey.dto.SurveyXml;
import io.github.darlene.surveyplatformbackend.survey.dto.SurveysXml;
import io.github.darlene.surveyplatformbackend.survey.model.Survey;
import io.github.darlene.surveyplatformbackend.survey.model.SurveyStatus;
import io.github.darlene.surveyplatformbackend.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;

    @Transactional(readOnly = true)
    public SurveysXml findAll() {
        return new SurveysXml(surveyRepository.findAll().stream().map(this::toXml).toList());
    }

    @Transactional(readOnly = true)
    public SurveyXml findById(Long surveyId) { return toXml(requireSurvey(surveyId)); }

    @Transactional
    public SurveyXml create(SurveyXml request) {
        Survey survey = new Survey();
        apply(survey, request);
        return toXml(surveyRepository.save(survey));
    }

    @Transactional
    public SurveyXml update(Long surveyId, SurveyXml request) {
        Survey survey = requireSurvey(surveyId);
        apply(survey, request);
        return toXml(surveyRepository.save(survey));
    }

    @Transactional
    public void delete(Long surveyId) { surveyRepository.delete(requireSurvey(surveyId)); }

    @Transactional
    public SurveyXml changeStatus(Long surveyId, SurveyStatus status) {
        Survey survey = requireSurvey(surveyId);
        if (status == SurveyStatus.LIVE && survey.getQuestions().isEmpty()) {
            throw new io.github.darlene.surveyplatformbackend.shared.exception.ValidationException(
                    "Add at least one question before publishing this interview"
            );
        }
        survey.setStatus(status);
        return toXml(surveyRepository.save(survey));
    }

    private Survey requireSurvey(Long id) {
        return surveyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Interview not found: " + id));
    }

    private void apply(Survey survey, SurveyXml request) {
        survey.setName(request.getName());
        survey.setDescription(request.getDescription());
    }

    private SurveyXml toXml(Survey survey) {
        SurveyXml xml = new SurveyXml();
        xml.setId(survey.getId());
        xml.setName(survey.getName());
        xml.setDescription(survey.getDescription());
        xml.setStatus(survey.getStatus().name());
        return xml;
    }
}
