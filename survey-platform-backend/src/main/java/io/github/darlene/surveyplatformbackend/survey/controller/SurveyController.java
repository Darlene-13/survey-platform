package io.github.darlene.surveyplatformbackend.survey.controller;

import io.github.darlene.surveyplatformbackend.survey.dto.SurveyXml;
import io.github.darlene.surveyplatformbackend.survey.dto.SurveysXml;
import io.github.darlene.surveyplatformbackend.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(value = "/api/v1/surveys", produces = APPLICATION_XML_VALUE)
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    @GetMapping public SurveysXml findAll() { return surveyService.findAll(); }
    @GetMapping("/{surveyId}") public SurveyXml findOne(@PathVariable Long surveyId) { return surveyService.findById(surveyId); }
    @PostMapping(consumes = APPLICATION_XML_VALUE) @ResponseStatus(HttpStatus.CREATED)
    public SurveyXml create(@RequestBody SurveyXml request) { return surveyService.create(request); }
    @PutMapping(value = "/{surveyId}", consumes = APPLICATION_XML_VALUE)
    public SurveyXml update(@PathVariable Long surveyId, @RequestBody SurveyXml request) { return surveyService.update(surveyId, request); }
    @DeleteMapping("/{surveyId}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long surveyId) { surveyService.delete(surveyId); }
}
