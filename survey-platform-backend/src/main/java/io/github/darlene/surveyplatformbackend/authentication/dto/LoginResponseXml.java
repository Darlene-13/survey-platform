package io.github.darlene.surveyplatformbackend.authentication.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "login_response")
public record LoginResponseXml(
        String token,
        UserXml user
) {}