package io.github.darlene.surveyplatformbackend.question.controller;

import io.github.darlene.surveyplatformbackend.question.dto.QuestionsXml;
import io.github.darlene.surveyplatformbackend.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(value = "/api/v1/surveys/{surveyId}/questions", produces = APPLICATION_XML_VALUE)
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    @GetMapping public QuestionsXml findAll(@PathVariable Long surveyId) { return questionService.findBySurvey(surveyId); }
    @GetMapping("/{questionId}") public io.github.darlene.surveyplatformbackend.question.dto.QuestionXml findOne(@PathVariable Long surveyId, @PathVariable Long questionId) { return questionService.findById(surveyId, questionId); }
    @PostMapping(consumes = APPLICATION_XML_VALUE) @ResponseStatus(HttpStatus.CREATED)
    public io.github.darlene.surveyplatformbackend.question.dto.QuestionXml create(@PathVariable Long surveyId, @RequestBody io.github.darlene.surveyplatformbackend.question.dto.QuestionXml request) { return questionService.create(surveyId, request); }
    @PutMapping(value = "/{questionId}", consumes = APPLICATION_XML_VALUE)
    public io.github.darlene.surveyplatformbackend.question.dto.QuestionXml update(@PathVariable Long surveyId, @PathVariable Long questionId, @RequestBody io.github.darlene.surveyplatformbackend.question.dto.QuestionXml request) { return questionService.update(surveyId, questionId, request); }
    @DeleteMapping("/{questionId}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long surveyId, @PathVariable Long questionId) { questionService.delete(surveyId, questionId); }
}
