package io.github.darlene.surveyplatformbackend.repository;

import io.github.darlene.surveyplatformbackend.domain.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    // GET /api/certificates/{id} needs nothing beyond the inherited findById —
    // it's reached directly by its own id, with no survey/response context in
    // the URL, which is exactly why this needed its own repository at all.
}