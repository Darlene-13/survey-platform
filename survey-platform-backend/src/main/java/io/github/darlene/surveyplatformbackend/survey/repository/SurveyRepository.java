package io.github.darlene.surveyplatformbackend.survey.repository;

import io.github.darlene.surveyplatformbackend.survey.model.Survey;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
