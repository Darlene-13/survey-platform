package io.github.darlene.surveyplatformbackend.response.controller;

import io.github.darlene.surveyplatformbackend.response.dto.QuestionResponsesXml;
import io.github.darlene.surveyplatformbackend.response.dto.QuestionResponseXml;
import io.github.darlene.surveyplatformbackend.response.dto.ResponseSubmissionXml;
import io.github.darlene.surveyplatformbackend.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionResponseXml submit(@PathVariable Long surveyId,
                                      @RequestPart("answers") ResponseSubmissionXml submission,
                                      @RequestParam MultiValueMap<String, MultipartFile> files,
                                      Authentication authentication) {
        return responseService.submit(surveyId, authentication.getName(), submission, files);
    }
}
