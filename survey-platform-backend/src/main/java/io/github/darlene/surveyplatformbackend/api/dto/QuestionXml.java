package io.github.darlene.surveyplatformbackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@JacksonXmlRootElement(localName = "question")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter @Setter @NoArgsConstructor
public class QuestionXml {

    @JacksonXmlProperty(isAttribute = true)
    private Long id;

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String type;                       // wire value: short_text, choice, ...

    @JacksonXmlProperty(isAttribute = true)
    private String required;                   // "yes" / "no"

    private String text;

    private String description;

    private OptionsXml options;                // only for choice questions

    @JacksonXmlProperty(localName = "file_properties")
    private FilePropertiesXml fileProperties;  // only for file questions
}