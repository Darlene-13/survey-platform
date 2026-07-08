package io.github.darlene.surveyplatformbackend.repository;

import io.github.darlene.surveyplatformbackend.domain.SurveyResponse;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.darlene.surveyplatformbackend.domain.SurveyResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {

    // Step 1: page over IDs only — no join, so LIMIT/OFFSET work correctly.
    @Query("""
        SELECT r.id FROM SurveyResponse r
        WHERE r.survey.id = :surveyId
          AND (:email IS NULL OR LOWER(r.emailAddress) LIKE LOWER(CONCAT('%', :email, '%')))
        ORDER BY r.dateResponded DESC
        """)
    Page<Long> findResponseIds(@Param("surveyId") Long surveyId,
                               @Param("email") String email,
                               Pageable pageable);

    // Step 2: fetch full data for exactly those IDs, joins are now safe
    // because there's no LIMIT/OFFSET on this query at all.
    @Query("""
        SELECT DISTINCT r FROM SurveyResponse r
        LEFT JOIN FETCH r.answers a
        LEFT JOIN FETCH a.question
        WHERE r.id IN :ids
        ORDER BY r.dateResponded DESC
        """)
    List<SurveyResponse> findWithAnswersByIdIn(@Param("ids") List<Long> ids);
}
