package io.github.darlene.surveyplatformbackend.question.service;

import io.github.darlene.surveyplatformbackend.question.dto.*;
import io.github.darlene.surveyplatformbackend.question.model.Question;
import io.github.darlene.surveyplatformbackend.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public QuestionsXml findBySurvey(Long surveyId) {
        return new QuestionsXml(questionRepository.findBySurveyIdOrderBySortOrderAscIdAsc(surveyId).stream().map(this::toXml).toList());
    }

    private QuestionXml toXml(Question question) {
        QuestionXml xml = new QuestionXml();
        xml.setId(question.getId());
        xml.setName(question.getName());
        xml.setType(question.getType().getWireValue());
        xml.setRequired(question.isRequired() ? "yes" : "no");
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
