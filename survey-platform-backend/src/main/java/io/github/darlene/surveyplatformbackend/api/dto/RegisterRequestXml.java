package io.github.darlene.surveyplatformbackend.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JacksonXmlRootElement(localName = "register_request")
@Getter @Setter @NoArgsConstructor
public class RegisterRequestXml {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}