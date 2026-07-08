package io.github.darlene.surveyplatformbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "responses")
@Getter @Setter @NoArgsConstructor
public class SurveyResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    /** Denormalized from the email answer for indexed search. */
    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "date_responded", nullable = false, updatable = false)
    private Instant dateResponded = Instant.now();

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResponseAnswer> answers = new ArrayList<>();

    public void addAnswer(ResponseAnswer answer) {
        answer.setResponse(this);
        answers.add(answer);
    }
}