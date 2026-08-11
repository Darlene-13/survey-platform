package io.github.darlene.surveyplatformbackend.question.service;

import io.github.darlene.surveyplatformbackend.question.dto.*;
import io.github.darlene.surveyplatformbackend.question.model.*;
import io.github.darlene.surveyplatformbackend.question.repository.QuestionRepository;
import io.github.darlene.surveyplatformbackend.shared.exception.ResourceNotFoundException;
import io.github.darlene.surveyplatformbackend.shared.exception.ValidationException;
import io.github.darlene.surveyplatformbackend.survey.model.Survey;
import io.github.darlene.surveyplatformbackend.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;

    @Transactional(readOnly = true)
    public QuestionsXml findBySurvey(Long surveyId) {
        requireSurvey(surveyId);
        return new QuestionsXml(questionRepository.findBySurveyIdOrderBySortOrderAscIdAsc(surveyId).stream().map(this::toXml).toList());
    }

    @Transactional(readOnly = true)
    public QuestionXml findById(Long surveyId, Long questionId) { return toXml(requireQuestion(surveyId, questionId)); }

    @Transactional
    public QuestionXml create(Long surveyId, QuestionXml request) {
        Survey survey = requireSurvey(surveyId);
        validate(request, surveyId, null);
        Question question = new Question();
        question.setSurvey(survey);
        apply(question, request);
        return toXml(questionRepository.save(question));
    }

    @Transactional
    public QuestionXml update(Long surveyId, Long questionId, QuestionXml request) {
        Question question = requireQuestion(surveyId, questionId);
        validate(request, surveyId, questionId);
        apply(question, request);
        return toXml(questionRepository.save(question));
    }

    @Transactional
    public void delete(Long surveyId, Long questionId) { questionRepository.delete(requireQuestion(surveyId, questionId)); }

    private void apply(Question question, QuestionXml request) {
        QuestionType type;
        try { type = QuestionType.fromWireValue(request.getType()); }
        catch (IllegalArgumentException exception) { throw new ValidationException(exception.getMessage()); }

        question.setName(request.getName().trim());
        question.setType(type);
        question.setRequired(yes(request.getRequired()));
        question.setText(request.getText().trim());
        question.setDescription(request.getDescription());
        question.setSortOrder(request.getSortOrder() == null ? 0 : request.getSortOrder());
        question.setAllowMultiple(type == QuestionType.CHOICE && request.getOptions() != null && yes(request.getOptions().getMultiple()));

        List<QuestionOption> options = new ArrayList<>();
        if (type == QuestionType.CHOICE && request.getOptions() != null) {
            int order = 0;
            for (OptionXml item : request.getOptions().getOption()) {
                QuestionOption option = new QuestionOption();
                option.setValue(item.getValue().trim());
                option.setLabel(item.getLabel().trim());
                option.setSortOrder(order++);
                options.add(option);
            }
        }
        question.replaceOptions(options);

        if (type == QuestionType.FILE) {
            FilePropertiesXml requestFile = request.getFileProperties();
            FileProperties file = question.getFileProperties() == null ? new FileProperties() : question.getFileProperties();
            file.setQuestion(question);
            file.setFormat(requestFile.getFormat().toLowerCase());
            file.setMaxFileSize(requestFile.getMaxFileSize());
            file.setMaxFileSizeUnit(requestFile.getMaxFileSizeUnit().toLowerCase());
            file.setAllowMultiple(yes(requestFile.getMultiple()));
            question.setFileProperties(file);
        } else {
            question.setFileProperties(null);
        }
    }

    private void validate(QuestionXml request, Long surveyId, Long questionId) {
        if (request.getName() == null || !request.getName().matches("[a-z][a-z0-9_]*"))
            throw new ValidationException("Question name must use lowercase letters, numbers, and underscores");
        if (request.getText() == null || request.getText().isBlank()) throw new ValidationException("Question text is required");
        if (request.getType() == null) throw new ValidationException("Question type is required");
        boolean duplicate = questionId == null
                ? questionRepository.existsBySurveyIdAndName(surveyId, request.getName())
                : questionRepository.existsBySurveyIdAndNameAndIdNot(surveyId, request.getName(), questionId);
        if (duplicate) throw new ValidationException("Question name already exists in this interview");
        if ("choice".equals(request.getType()) && (request.getOptions() == null || request.getOptions().getOption().isEmpty()))
            throw new ValidationException("Choice questions require at least one option");
        if ("file".equals(request.getType())) {
            FilePropertiesXml file = request.getFileProperties();
            if (file == null || file.getFormat() == null || file.getMaxFileSize() == null || file.getMaxFileSize() < 1)
                throw new ValidationException("File questions require valid file properties");
        }
    }

    private Survey requireSurvey(Long id) {
        return surveyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Interview not found: " + id));
    }

    private Question requireQuestion(Long surveyId, Long questionId) {
        return questionRepository.findByIdAndSurveyId(questionId, surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found: " + questionId));
    }

    private boolean yes(String value) { return "yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value); }

    private QuestionXml toXml(Question question) {
        QuestionXml xml = new QuestionXml();
        xml.setId(question.getId());
        xml.setName(question.getName());
        xml.setType(question.getType().getWireValue());
        xml.setRequired(question.isRequired() ? "yes" : "no");
        xml.setSortOrder(question.getSortOrder());
        xml.setText(question.getText());
        xml.setDescription(question.getDescription());
        if (!question.getOptions().isEmpty()) {
            OptionsXml options = new OptionsXml();
            options.setMultiple(question.isAllowMultiple() ? "yes" : "no");
            options.setOption(question.getOptions().stream().map(option -> {
                OptionXml value = new OptionXml(); value.setValue(option.getValue()); value.setLabel(option.getLabel()); return value;
            }).toList());
            xml.setOptions(options);
        }
        if (question.getFileProperties() != null) {
            FilePropertiesXml file = new FilePropertiesXml();
            file.setFormat(question.getFileProperties().getFormat());
            file.setMaxFileSize(question.getFileProperties().getMaxFileSize());
            file.setMaxFileSizeUnit(question.getFileProperties().getMaxFileSizeUnit());
            file.setMultiple(question.getFileProperties().isAllowMultiple() ? "yes" : "no");
            xml.setFileProperties(file);
        }
        return xml;
    }
}
