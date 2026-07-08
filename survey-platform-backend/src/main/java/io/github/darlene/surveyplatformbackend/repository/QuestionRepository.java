package io.github.darlene.surveyplatformbackend.repository;

import io.github.darlene.surveyplatformbackend.domain.Question;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @EntityGraph(attributePaths = {"options", "fileProperties"})
    List<Question> findBySurveyIdOrderBySortOrderAscIdAsc(Long surveyId);

    Optional<Question> findByIdAndSurveyId(Long id, Long surveyId);

    boolean existsBySurveyIdAndName(Long surveyId, String name);
}
