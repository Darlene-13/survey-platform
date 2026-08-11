package io.github.darlene.surveyplatformbackend.authentication.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.github.darlene.surveyplatformbackend.authentication.model.UserRole;
import lombok.Builder;

import java.time.LocalDateTime;

@JacksonXmlRootElement(localName = "login_response")
@Builder
public record LoginResponseXml(
        String token,
        UserXml user,
        String type,
        LocalDateTime expiresIn,
        UserRole role,
        String email,
        String refreshToken
) {}