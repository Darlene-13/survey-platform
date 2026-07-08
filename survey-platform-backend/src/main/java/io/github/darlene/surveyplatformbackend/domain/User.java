package io.github.darlene.surveyplatformbackend.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name="users")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name ="first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique=true)
    private String email;

    @Column(name="hashed_password", nullable = false)
    private String hashedPassword;

    @Column(name="role", nullable = false)
    private UserRole role;

    @CreationTimestamp
    @Column(name="creation_at")
    private LocalDateTime createdAt;


}