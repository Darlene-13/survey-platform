package io.github.darlene.surveyplatformbackend.configuration;

import io.github.darlene.surveyplatformbackend.authentication.model.User;
import io.github.darlene.surveyplatformbackend.authentication.model.UserRole;
import io.github.darlene.surveyplatformbackend.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.local-admin.enabled", havingValue = "true")
public class LocalAdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.local-admin.email}") private String email;
    @Value("${app.local-admin.password}") private String password;
    @Value("${app.local-respondent.email}") private String respondentEmail;
    @Value("${app.local-respondent.password}") private String respondentPassword;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmailIgnoreCase(email).isEmpty()) {
            userRepository.save(User.builder()
                    .firstName("Local")
                    .lastName("Admin")
                    .email(email.toLowerCase())
                    .hashedPassword(passwordEncoder.encode(password))
                    .role(UserRole.ADMIN)
                    .build());
        }
        if (userRepository.findByEmailIgnoreCase(respondentEmail).isEmpty()) {
            userRepository.save(User.builder()
                    .firstName("Local")
                    .lastName("Respondent")
                    .email(respondentEmail.toLowerCase())
                    .hashedPassword(passwordEncoder.encode(respondentPassword))
                    .role(UserRole.RESPONDENT)
                    .build());
        }
    }
}
