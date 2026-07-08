package io.github.darlene.surveyplatformbackend.api.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JacksonXmlRootElement(localName = "survey")
@Getter @Setter @NoArgsConstructor
public class SurveyXml {

    @JacksonXmlProperty(isAttribute = true)
    private Long id;

    private String name;

    private String description;
}
