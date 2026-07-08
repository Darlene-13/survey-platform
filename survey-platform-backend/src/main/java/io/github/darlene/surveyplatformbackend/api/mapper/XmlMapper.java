package io.github.darlene.surveyplatformbackend.mapper;

import io.github.darlene.surveyplatformbackend.dto.*;
import io.github.darlene.surveyplatformbackend.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class XmlMapper {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);

    public SurveyXml toXml(Survey survey) {
        SurveyXml xml = new SurveyXml();
        xml.setId(survey.getId());
        xml.setName(survey.getName());
        xml.setDescription(survey.getDescription());
        return xml;
    }

    public SurveysXml toSurveysXml(List<Survey> surveys) {
        return new SurveysXml(surveys.stream().map(this::toXml).toList());
    }


    public QuestionXml toXml(Question question) {
        QuestionXml xml = new QuestionXml();
        xml.setId(question.getId());
        xml.setName(question.getName());
        xml.setType(question.getType().getWireValue()); // fixed: getWireValue(), not wireValue()
        xml.setRequired(yesNo(question.isRequired()));
        xml.setText(question.getText());
        xml.setDescription(question.getDescription());

        if (question.getType() == QuestionType.CHOICE) {
            OptionsXml options = new OptionsXml();
            options.setMultiple(yesNo(question.isAllowMultiple()));
            options.setOption(question.getOptions().stream().map(o -> {
                OptionXml option = new OptionXml();
                option.setValue(o.getValue());
                option.setLabel(o.getLabel());
                return option;
            }).toList());
            xml.setOptions(options);
        }

        if (question.getType() == QuestionType.FILE && question.getFileProperties() != null) {
            FileProperties fp = question.getFileProperties();
            FilePropertiesXml fpXml = new FilePropertiesXml();
            fpXml.setFormat(fp.getFormat());
            fpXml.setMaxFileSize(fp.getMaxFileSize());
            fpXml.setMaxFileSizeUnit(fp.getMaxFileSizeUnit());
            fpXml.setMultiple(yesNo(fp.isAllowMultiple()));
            xml.setFileProperties(fpXml);
        }

        return xml;
    }

    public QuestionsXml toQuestionsXml(List<Question> questions) {
        return new QuestionsXml(questions.stream().map(this::toXml).toList());
    }

    public QuestionResponseXml toXml(SurveyResponse response) {
        List<CertificateXml> certs = response.getAnswers().stream()
                .flatMap(a -> a.getCertificates().stream())
                .map(c -> new CertificateXml(c.getId(), c.getFileName()))
                .toList();

        QuestionResponseXml xml = new QuestionResponseXml(
                response.getId(),
                certs.isEmpty() ? null : new CertificatesXml(certs),
                DATE_FORMAT.format(response.getDateResponded()));

        response.getAnswers().stream()
                .filter(a -> a.getQuestion().getType() != QuestionType.FILE)
                .sorted((a, b) -> Integer.compare(
                        a.getQuestion().getSortOrder(), b.getQuestion().getSortOrder()))
                .forEach(a -> xml.putAnswer(a.getQuestion().getName(), a.getAnswerValue()));

        return xml;
    }

    public QuestionResponsesXml toResponsesXml(Page<SurveyResponse> page) {
        return new QuestionResponsesXml(
                page.getNumber() + 1,                       // Spring is 0-based, contract is 1-based
                Math.max(page.getTotalPages(), 1),
                page.getSize(),
                page.getTotalElements(),
                page.getContent().stream().map(this::toXml).toList());
    }

    private String yesNo(boolean value) {
        return value ? "yes" : "no";
    }
}