package io.github.darlene.surveyplatformbackend.question.repository;

import io.github.darlene.surveyplatformbackend.question.model.Question;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @EntityGraph(attributePaths = {"options", "fileProperties"})
    List<Question> findBySurveyIdOrderBySortOrderAscIdAsc(Long surveyId);

    Optional<Question> findByIdAndSurveyId(Long id, Long surveyId);

    boolean existsBySurveyIdAndName(Long surveyId, String name);

    boolean existsBySurveyIdAndNameAndIdNot(Long surveyId, String name, Long id);
}
