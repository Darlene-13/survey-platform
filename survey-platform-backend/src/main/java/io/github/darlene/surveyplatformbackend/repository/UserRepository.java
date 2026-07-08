package io.github.darlene.surveyplatformbackend.repository;

import io.github.darlene.surveyplatformbackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Login and registration both need to look a user up by email rather than
    // by id — this is the entry point before you have an id to work with.
    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}