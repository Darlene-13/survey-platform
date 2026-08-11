package io.github.darlene.surveyplatformbackend.response.controller;

import io.github.darlene.surveyplatformbackend.response.dto.QuestionResponsesXml;
import io.github.darlene.surveyplatformbackend.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(value = "/api/v1/surveys/{surveyId}/responses", produces = APPLICATION_XML_VALUE)
@RequiredArgsConstructor
public class ResponseController {
    private final ResponseService responseService;

    @GetMapping
    public QuestionResponsesXml findAll(@PathVariable Long surveyId,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int pageSize,
                                        @RequestParam(required = false) String email) {
        return responseService.findBySurvey(surveyId, page, pageSize, email);
    }
}
