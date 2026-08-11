package io.github.darlene.surveyplatformbackend.authentication.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@JacksonXmlRootElement(localName = "refresh_token")
@Data @NoArgsConstructor @AllArgsConstructor
public class RefreshTokenXml{

    @NotBlank(message = "Refresh token is required")
    private String refreshToken;

}
