package io.github.darlene.surveyplatformbackend.response.service;

import io.github.darlene.surveyplatformbackend.certificate.dto.CertificateXml;
import io.github.darlene.surveyplatformbackend.certificate.dto.CertificatesXml;
import io.github.darlene.surveyplatformbackend.response.dto.QuestionResponseXml;
import io.github.darlene.surveyplatformbackend.response.dto.QuestionResponsesXml;
import io.github.darlene.surveyplatformbackend.response.model.SurveyResponse;
import io.github.darlene.surveyplatformbackend.response.repository.SurveyResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponseService {
    private final SurveyResponseRepository responseRepository;

    @Transactional(readOnly = true)
    public QuestionResponsesXml findBySurvey(Long surveyId, int page, int pageSize, String email) {
        Page<Long> ids = responseRepository.findResponseIds(surveyId, blankToNull(email), PageRequest.of(page, pageSize));
        List<QuestionResponseXml> responses = ids.isEmpty() ? List.of() : responseRepository.findWithAnswersByIdIn(ids.getContent()).stream().map(this::toXml).toList();
        return new QuestionResponsesXml(ids.getNumber(), ids.getTotalPages(), ids.getSize(), ids.getTotalElements(), responses);
    }

    private QuestionResponseXml toXml(SurveyResponse response) {
        List<CertificateXml> certificates = response.getAnswers().stream()
                .flatMap(answer -> answer.getCertificates().stream())
                .map(file -> new CertificateXml(file.getId(), file.getFileName()))
                .toList();
        QuestionResponseXml xml = new QuestionResponseXml(response.getId(), new CertificatesXml(certificates), response.getDateResponded().toString());
        response.getAnswers().forEach(answer -> xml.putAnswer(answer.getQuestion().getName(), answer.getAnswerValue()));
        return xml;
    }

    private String blankToNull(String value) { return value == null || value.isBlank() ? null : value.trim(); }
}
