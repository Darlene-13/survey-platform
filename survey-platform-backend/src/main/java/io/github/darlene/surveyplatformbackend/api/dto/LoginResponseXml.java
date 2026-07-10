package io.github.darlene.surveyplatformbackend.api.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "login_response")
public record LoginResponseXml(
        String token,
        UserXml user
) {}