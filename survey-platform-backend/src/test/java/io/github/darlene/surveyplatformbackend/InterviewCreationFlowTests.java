package io.github.darlene.surveyplatformbackend;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.darlene.surveyplatformbackend.authentication.dto.LoginResponseXml;
import io.github.darlene.surveyplatformbackend.survey.dto.SurveyXml;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InterviewCreationFlowTests {
    @Autowired MockMvc mockMvc;
    private final XmlMapper xmlMapper = new XmlMapper();

    @Test
    void adminCanCreateAndPublishThenRespondentCanSubmit() throws Exception {
        String adminToken = login("admin@respondly.local", "Admin123!");
        String createdXml = mockMvc.perform(post("/api/v1/surveys")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_XML)
                        .content("<survey><name>Test Interview</name><description>Integration flow</description></survey>"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        SurveyXml survey = xmlMapper.readValue(createdXml, SurveyXml.class);

        mockMvc.perform(post("/api/v1/surveys/{surveyId}/questions", survey.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_XML)
                        .content("<question name=\"motivation\" type=\"long_text\" required=\"yes\" sort_order=\"0\"><text>Why do you want this role?</text><description>Be specific.</description></question>"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/surveys/{surveyId}/publish", survey.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());

        String respondentToken = login("respondent@respondly.local", "Respondent123!");
        MockMultipartFile answers = new MockMultipartFile("answers", "answers.xml", MediaType.APPLICATION_XML_VALUE,
                "<response><answer question=\"motivation\">I enjoy solving useful problems.</answer></response>".getBytes());
        mockMvc.perform(multipart("/api/v1/surveys/{surveyId}/responses", survey.getId())
                        .file(answers)
                        .header("Authorization", "Bearer " + respondentToken)
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isCreated());
    }

    private String login(String email, String password) throws Exception {
        String xml = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_XML)
                        .content("<login_request><email>" + email + "</email><password>" + password + "</password></login_request>"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return xmlMapper.readValue(xml, LoginResponseXml.class).token();
    }
}
