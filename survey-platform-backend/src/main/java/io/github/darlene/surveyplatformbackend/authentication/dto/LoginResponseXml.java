package io.github.darlene.surveyplatformbackend.authentication.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.github.darlene.surveyplatformbackend.authentication.model.UserRole;
import lombok.Builder;

@JacksonXmlRootElement(localName = "login_response")
@Builder
public record LoginResponseXml(
        String token,
        UserXml user,
        String type,
        String expiresIn,
        UserRole role,
        String email,
        String refreshToken
) {}
