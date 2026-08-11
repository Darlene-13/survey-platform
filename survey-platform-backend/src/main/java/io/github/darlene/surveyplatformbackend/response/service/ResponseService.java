package io.github.darlene.surveyplatformbackend.response.service;

import io.github.darlene.surveyplatformbackend.authentication.model.User;
import io.github.darlene.surveyplatformbackend.authentication.repository.UserRepository;
import io.github.darlene.surveyplatformbackend.certificate.dto.CertificateXml;
import io.github.darlene.surveyplatformbackend.certificate.dto.CertificatesXml;
import io.github.darlene.surveyplatformbackend.certificate.model.Certificate;
import io.github.darlene.surveyplatformbackend.question.model.Question;
import io.github.darlene.surveyplatformbackend.question.model.QuestionType;
import io.github.darlene.surveyplatformbackend.question.repository.QuestionRepository;
import io.github.darlene.surveyplatformbackend.response.dto.*;
import io.github.darlene.surveyplatformbackend.response.model.ResponseAnswer;
import io.github.darlene.surveyplatformbackend.response.model.SurveyResponse;
import io.github.darlene.surveyplatformbackend.response.repository.SurveyResponseRepository;
import io.github.darlene.surveyplatformbackend.shared.exception.InvalidFileException;
import io.github.darlene.surveyplatformbackend.shared.exception.ResourceNotFoundException;
import io.github.darlene.surveyplatformbackend.shared.exception.ValidationException;
import io.github.darlene.surveyplatformbackend.survey.model.Survey;
import io.github.darlene.surveyplatformbackend.survey.model.SurveyStatus;
import io.github.darlene.surveyplatformbackend.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResponseService {
    private final SurveyResponseRepository responseRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public QuestionResponsesXml findBySurvey(Long surveyId, int page, int pageSize, String email) {
        if (page < 0 || pageSize < 1 || pageSize > 100) throw new ValidationException("Page size must be between 1 and 100");
        Page<Long> ids = responseRepository.findResponseIds(surveyId, blankToNull(email), PageRequest.of(page, pageSize));
        List<QuestionResponseXml> responses = ids.isEmpty() ? List.of() : responseRepository.findWithAnswersByIdIn(ids.getContent()).stream().map(this::toXml).toList();
        return new QuestionResponsesXml(ids.getNumber(), ids.getTotalPages(), ids.getSize(), ids.getTotalElements(), responses);
    }

    @Transactional
    public QuestionResponseXml submit(Long surveyId, String email, ResponseSubmissionXml submission,
                                      MultiValueMap<String, MultipartFile> uploadedFiles) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found: " + surveyId));
        if (survey.getStatus() != SurveyStatus.LIVE) throw new ValidationException("This interview is not accepting responses");
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        List<Question> questions = questionRepository.findBySurveyIdOrderBySortOrderAscIdAsc(surveyId);
        Map<String, Question> questionsByName = questions.stream().collect(Collectors.toMap(Question::getName, Function.identity()));
        Map<String, String> values = new LinkedHashMap<>();
        for (AnswerSubmissionXml answer : submission.getAnswers()) {
            if (!questionsByName.containsKey(answer.getQuestion())) throw new ValidationException("Unknown question: " + answer.getQuestion());
            if (values.put(answer.getQuestion(), answer.getValue()) != null) throw new ValidationException("Duplicate answer: " + answer.getQuestion());
        }

        SurveyResponse response = new SurveyResponse();
        response.setSurvey(survey);
        response.setUser(user);
        for (Question question : questions) {
            String value = blankToNull(values.get(question.getName()));
            List<MultipartFile> files = uploadedFiles.getOrDefault(question.getName(), List.of()).stream().filter(file -> !file.isEmpty()).toList();
            if (question.isRequired() && value == null && files.isEmpty()) throw new ValidationException("Answer required for: " + question.getName());
            if (value == null && files.isEmpty()) continue;
            if (question.getType() != QuestionType.FILE && !files.isEmpty()) throw new InvalidFileException("Files are not allowed for: " + question.getName());

            ResponseAnswer answer = new ResponseAnswer();
            answer.setQuestion(question);
            answer.setAnswerValue(question.getType() == QuestionType.FILE ? fileNames(files) : value);
            if (question.getType() == QuestionType.FILE) addCertificates(question, answer, files);
            response.addAnswer(answer);
            if (question.getType() == QuestionType.EMAIL && value != null) response.setEmailAddress(value.trim().toLowerCase());
        }
        if (response.getEmailAddress() == null) response.setEmailAddress(user.getEmail());
        return toXml(responseRepository.save(response));
    }

    private void addCertificates(Question question, ResponseAnswer answer, List<MultipartFile> files) {
        if (question.getFileProperties() == null) throw new InvalidFileException("File rules are missing for: " + question.getName());
        if (!question.getFileProperties().isAllowMultiple() && files.size() > 1) throw new InvalidFileException("Only one file is allowed for: " + question.getName());
        for (MultipartFile file : files) {
            String filename = Optional.ofNullable(file.getOriginalFilename()).orElse("certificate");
            if (!filename.toLowerCase().endsWith(question.getFileProperties().getFormat().toLowerCase()))
                throw new InvalidFileException("Invalid file format for: " + question.getName());
            if (file.getSize() > question.getFileProperties().maxSizeInBytes())
                throw new InvalidFileException("File is too large for: " + question.getName());
            Certificate certificate = new Certificate();
            certificate.setFileName(filename);
            certificate.setContentType(Optional.ofNullable(file.getContentType()).orElse("application/octet-stream"));
            certificate.setFileSize(file.getSize());
            try { certificate.setFileData(file.getBytes()); }
            catch (IOException exception) { throw new InvalidFileException("Could not read file: " + filename); }
            answer.addCertificate(certificate);
        }
    }

    private String fileNames(List<MultipartFile> files) {
        return files.stream().map(file -> Optional.ofNullable(file.getOriginalFilename()).orElse("certificate")).collect(Collectors.joining(","));
    }

    private QuestionResponseXml toXml(SurveyResponse response) {
        List<CertificateXml> certificates = response.getAnswers().stream().flatMap(answer -> answer.getCertificates().stream())
                .map(file -> new CertificateXml(file.getId(), file.getFileName())).toList();
        QuestionResponseXml xml = new QuestionResponseXml(response.getId(), new CertificatesXml(certificates), response.getDateResponded().toString());
        response.getAnswers().forEach(answer -> xml.putAnswer(answer.getQuestion().getName(), answer.getAnswerValue()));
        return xml;
    }

    private String blankToNull(String value) { return value == null || value.isBlank() ? null : value.trim(); }
}
