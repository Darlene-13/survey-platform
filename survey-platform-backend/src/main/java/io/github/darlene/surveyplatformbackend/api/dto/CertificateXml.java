package io.github.darlene.surveyplatformbackend.api.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public record CertificateXml(
        @JacksonXmlProperty(isAttribute = true) Long id,
        @JacksonXmlText String fileName
) {}
