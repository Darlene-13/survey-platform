package io.github.darlene.surveyplatformbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "response_answers")
@Getter @Setter @NoArgsConstructor
public class ResponseAnswer{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Delay loading until they are accessed
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "response_id")
    private SurveyResponse response;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    /** Text answer or comma joined value for multi choice**/
    @Column(name = "answer_value", columnDefinition = "text")
    private String answerValue;

    @OneToMany(mappedBy="answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certificate> certificates = new ArrayList<>();

    //method to add certificate
    public void addCertificate(Certificate certificate){
        certificate.setAnswer(this);
        certificates.add(certificate);
    }

}