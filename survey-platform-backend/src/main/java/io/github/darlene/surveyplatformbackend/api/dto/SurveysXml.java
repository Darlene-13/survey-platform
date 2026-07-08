package io.github.darlene.surveyplatformbackend.api.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;


@JacksonXmlRootElement(localName = "surveys")
public record SurveysXml(
    @JacksonXmlElementWrapper
    @JacksonXmlProperty(localName="survey")
    List<SurveyXml> surveys){}



