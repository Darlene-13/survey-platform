package io.github.darlene.surveyplatformbackend.service;


import io.github.darlene.surveyplatformbackend.domain.Question;
import io.github.darlene.surveyplatformbackend.domain.QuestionType;
import io.github.darlene.surveyplatformbackend.domain.Survey;
import io.github.darlene.surveyplatformbackend.exceptions.NotFoundException;
import io.github.darlene.surveyplatformbackend.exceptions.ValidationException;
import org.springframework.stereotype.Service;
import io.github.darlene.surveyplatformbackend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService{

    private final QuestionRepository questionRepository;
    private final SurveyService surveyService;

    public List<Question> findBySurvey(Long surveyId){
        surveyService.getById(surveyId);    // 404 for unknown survey and not an empty
        return questionRepository.findBySurveyIdOrderBySortOrderAscIdAsc(surveyId);
    }

    @Transactional
    public Question create(Long surveyId, Question question){
        Survey survey = surveyService.getById(surveyId);

        if(questionRepository.existsBySurveyIdAndName(surveyId, question.getName())) {
            throw new ValidationException(
                    "A question named " + question.getName() + " already exists in this survey"
            );
        }
            validateShape(question);

            question.setSurvey(survey);
            question.setSortOrder(survey.getQuestions().size());
            wireChildren(question);
            return questionRepository.save(question);

    }

    @Transactional
    public void delete(Long surveyId, Long questionId){
        Question question = questionRepository.findByIdAndSurveyId(questionId, surveyId)
                .orElseThrow(() -> new NotFoundException("Question", questionId));
        questionRepository.delete(question);

    }


    // Private method for shape validation
    private void validateShape(Question question){
        if(question.getType() == QuestionType.CHOICE && question.getOptions().isEmpty()){
            throw new ValidationException("Choice questions require at least one option");
        }
        if(question.getType() == QuestionType.CHOICE && !question.getOptions().isEmpty()){
            throw new ValidationException("Only choice questions may have options");
        }
        if(question.getType() == QuestionType.FILE && question.getFileProperties() ==  null){
            throw new ValidationException("File questions require file properties");
        }
    }

    private void wireChildren(Question question){
        question.getOptions().forEach(o -> o.setQuestion(question));
        if(question.getFileProperties() != null){
            question.getFileProperties().setQuestion(question);
        }
    }

}
