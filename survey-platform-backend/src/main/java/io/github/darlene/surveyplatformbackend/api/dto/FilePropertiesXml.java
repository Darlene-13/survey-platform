package io.github.darlene.surveyplatformbackend.api.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class FilePropertiesXml {

    @JacksonXmlProperty(isAttribute = true)
    private String format;

    @JacksonXmlProperty(isAttribute = true, localName = "max_file_size")
    private Integer maxFileSize;

    @JacksonXmlProperty(isAttribute = true, localName = "max_file_size_unit")
    private String maxFileSizeUnit;

    @JacksonXmlProperty(isAttribute = true)
    private String multiple;
}