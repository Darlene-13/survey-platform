package io.github.darlene.surveyplatformbackend.repository;

import io.github.darlene.surveyplatformbackend.domain.Survey;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
