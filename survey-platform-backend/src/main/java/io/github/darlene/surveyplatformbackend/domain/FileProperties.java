package io.github.darlene.surveyplatformbackend.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "file_properties")
@Getter @Setter @NoArgsConstructor
public class FileProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", unique = true)
    private Question question;

    @Column(nullable = false, length = 20)
    private String format = ".pdf";

    @Column(name = "max_file_size", nullable = false)
    private int maxFileSize = 1;

    @Column(name = "max_file_size_unit", nullable = false, length = 10)
    private String maxFileSizeUnit = "mb";

    @Column(name = "allow_multiple", nullable = false)
    private boolean allowMultiple = true;

    public long maxSizeInBytes() {
        long multiplier = "kb".equalsIgnoreCase(maxFileSizeUnit) ? 1024L : 1024L * 1024L;
        return maxFileSize * multiplier;
    }
}
