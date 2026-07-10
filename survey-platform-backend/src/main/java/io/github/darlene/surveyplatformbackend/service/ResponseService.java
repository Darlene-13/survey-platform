package io.github.darlene.surveyplatformbackend.service;


import io.github.darlene.surveyplatformbackend.domain.*;
import io.github.darlene.surveyplatformbackend.repository.QuestionRepository;
import io.github.darlene.surveyplatformbackend.repository.SurveyRepository;
import io.github.darlene.surveyplatformbackend.repository.SurveyResponseRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResponseService {

    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,6}$");
    private static final byte[] PDF_MAGIC = {'%', 'P', 'D', 'F'};


    private final SurveyRepository surveyRepository;
    private final SurveyService surveyService;
    private final SurveyResponseRepository responseRepository;
    private final QuestionRepository questionRepository;

    /**
     * Persists multipart submission after validating every field against
     * The survey's question metadata. One transaction: response, answers,
     * and certificates commit or rollback together
     */

    @Transactional
    public SurveyResponse submit(Long surveyId, Map<String, String> fields, List<MultiPartFile>files){
        // Get survey by id
        Survey survey = surveyService.getById(surveyId);
        // Get the question
        List<Question> questions = questionRepository.findBySurveyIdOrderBySortOrderAscIdAsc(surveyId);
        // Reject unknown fields
        rejectUnknownFields(fields, questions);

        SurveyResponse response = new SurveyResponse();
        response.setSurvey(survey);

        for(Question question: questions){
            switch(question.getType()){
                case FILE -> response.addAnswer(buildFileAnswer(question, files));
                default -> response.addAnswer(buildTextAnswer(question, fields));
            }
        }

        questions.stream()
                .filter(q ->q.getType() == QuestionType.EMAIL)
                .findFirst()
                .ifPresent(q -> response.setEmailAddress(fields.get(q.getName())));

        return responseRepository.save(response);
    }

    public Page<SurveyResponse> findResponse(Long surveyId, int page, int pageSize, String email){
        surveyService.getById(surveyId);
        // Find the response by user email
        String normalizedEmail = StringUtils.hasText(email)
    }

    private ResponseAnswer buildTextAnswer(Question question, Map<String, String> fields) {

    }

    private ResponseAnswer buildFileAnswer(Question question, List<MultiPartFile> files) {

    }

    private void rejectUnknownFields(Map<String, String> fields, List<Question> questions) {

    }
}
