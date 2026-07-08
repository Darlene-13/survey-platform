package io.github.darlene.surveyplatformbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"survey_id", "name"}))
@Getter @Setter @NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    /** Machine name, e.g. "full_name" — the key used in submissions and responses. */
    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private QuestionType type;

    @Column(nullable = false)
    private boolean required = true;

    @Column(nullable = false, columnDefinition = "text")
    private String text;

    @Column(columnDefinition = "text")
    private String description;

    /** Only meaningful for CHOICE questions. */
    @Column(name = "allow_multiple", nullable = false)
    private boolean allowMultiple = false;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder = 0;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC, id ASC")
    private List<QuestionOption> options = new ArrayList<>();

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private FileProperties fileProperties;

    public void replaceOptions(List<QuestionOption> newOptions) {
        options.clear();
        newOptions.forEach(o -> {
            o.setQuestion(this);
            options.add(o);
        });
    }
}
