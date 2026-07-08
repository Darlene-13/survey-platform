package io.github.darlene.surveyplatformbackend.api.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class OptionXml {

    @JacksonXmlProperty(isAttribute = true)
    private String value;

    @JacksonXmlText
    private String label;

}