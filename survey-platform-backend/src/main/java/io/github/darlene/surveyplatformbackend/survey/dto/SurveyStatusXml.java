package io.github.darlene.surveyplatformbackend.survey.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JacksonXmlRootElement(localName = "survey_status")
@Getter
@Setter
@NoArgsConstructor
public class SurveyStatusXml {
    private String status;
}
