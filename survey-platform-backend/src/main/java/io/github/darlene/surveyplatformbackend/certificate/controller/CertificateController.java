package io.github.darlene.surveyplatformbackend.certificate.controller;

import io.github.darlene.surveyplatformbackend.certificate.model.Certificate;
import io.github.darlene.surveyplatformbackend.certificate.repository.CertificateRepository;
import io.github.darlene.surveyplatformbackend.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/certificates")
@RequiredArgsConstructor
public class CertificateController {
    private final CertificateRepository certificateRepository;

    @GetMapping("/{certificateId}")
    public ResponseEntity<byte[]> download(@PathVariable Long certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found: " + certificateId));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(certificate.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(certificate.getFileName()).build().toString())
                .contentLength(certificate.getFileSize())
                .body(certificate.getFileData());
    }
}
