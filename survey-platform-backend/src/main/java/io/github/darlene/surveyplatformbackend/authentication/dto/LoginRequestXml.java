package io.github.darlene.surveyplatformbackend.authentication.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@JacksonXmlRootElement(localName = "login_request")
@Getter @Setter @NoArgsConstructor
public class LoginRequestXml {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
}
