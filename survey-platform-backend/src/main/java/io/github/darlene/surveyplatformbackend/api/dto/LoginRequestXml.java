package io.github.darlene.surveyplatformbackend.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JacksonXmlRootElement(localName = "login_request")
@Getter @Setter @NoArgsConstructor
public class LoginRequestXml {
    private String email;
    private String password;
}