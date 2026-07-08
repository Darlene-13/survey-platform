package io.github.darlene.surveyplatformbackend.api.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;


import java.util.LinkedHashMap;
import java.util.Map;

@JacksonXmlRootElement(localName = "question_response")
@Getter
public class QuestionResponseXml {

    @JsonProperty("response_id")
    private final Long responseId;

    /** Dynamic answers keyed by question name — serialized as sibling elements. */
    private final Map<String, String> answers = new LinkedHashMap<>();

    private final CertificatesXml certificates;

    @JsonProperty("date_responded")
    private final String dateResponded;

    public QuestionResponseXml(Long responseId, CertificatesXml certificates, String dateResponded) {
        this.responseId = responseId;
        this.certificates = certificates;
        this.dateResponded = dateResponded;
    }

    public void putAnswer(String questionName, String value) {
        answers.put(questionName, value);
    }

    @JsonAnyGetter
    public Map<String, String> getAnswers() {
        return answers;
    }
}