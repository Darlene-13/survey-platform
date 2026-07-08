package io.github.darlene.surveyplatformbackend.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "user")
public record UserXml(
        @JacksonXmlProperty(isAttribute = true) Long id,
        @JacksonXmlProperty(localName = "first_name") String firstName,
        @JacksonXmlProperty(localName = "last_name") String lastName,
        String email,
        String role
) {}