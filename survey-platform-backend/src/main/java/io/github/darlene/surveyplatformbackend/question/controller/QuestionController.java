package io.github.darlene.surveyplatformbackend.question.controller;

import io.github.darlene.surveyplatformbackend.question.dto.QuestionsXml;
import io.github.darlene.surveyplatformbackend.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(value = "/api/v1/surveys/{surveyId}/questions", produces = APPLICATION_XML_VALUE)
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    @GetMapping public QuestionsXml findAll(@PathVariable Long surveyId) { return questionService.findBySurvey(surveyId); }
}
