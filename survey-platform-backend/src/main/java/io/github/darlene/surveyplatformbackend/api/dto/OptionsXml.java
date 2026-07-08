package io.github.darlene.surveyplatformbackend.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor

public class OptionsXml {

    @JacksonXmlProperty(isAttribute = true)
    private String multiple;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "option")
    private List<OptionalXml> option = new ArrayList<>();
}
