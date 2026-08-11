package io.github.darlene.surveyplatformbackend.authentication.service;

import io.github.darlene.surveyplatformbackend.authentication.model.User;
import io.github.darlene.surveyplatformbackend.authentication.repository.UserRepository;
import io.github.darlene.surveyplatformbackend.shared.exception.EmailNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsImplementation implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new EmailNotFoundException(email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getHashedPassword())
                .authorities(user.getRole().name())
                .build();
    }
}
