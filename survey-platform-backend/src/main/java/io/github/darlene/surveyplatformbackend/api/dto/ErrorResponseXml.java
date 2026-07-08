package io.github.darlene.surveyplatformbackend.exception;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "error")
public record ErrorResponseXml(
        @JacksonXmlProperty(isAttribute = true) int status,
        String message,
        String path,
        String timestamp
) {}