package io.github.darlene.surveyplatformbackend.response.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerSubmissionXml {
    @JacksonXmlProperty(isAttribute = true)
    private String question;

    @JacksonXmlText
    private String value;
}
