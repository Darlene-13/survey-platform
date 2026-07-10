package io.github.darlene.surveyplatformbackend.api.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "question_responses")
public record QuestionResponsesXml(
        @JacksonXmlProperty(isAttribute = true, localName = "current_page") int currentPage,
        @JacksonXmlProperty(isAttribute = true, localName = "last_page") int lastPage,
        @JacksonXmlProperty(isAttribute = true, localName = "page_size") int pageSize,
        @JacksonXmlProperty(isAttribute = true, localName = "total_count") long totalCount,

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "question_response")
        List<QuestionResponseXml> responses
) {}