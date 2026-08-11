package io.github.darlene.surveyplatformbackend.response.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "response")
@Getter
@Setter
@NoArgsConstructor
public class ResponseSubmissionXml {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "answer")
    private List<AnswerSubmissionXml> answers = new ArrayList<>();
}
