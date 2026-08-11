package io.github.darlene.surveyplatformbackend.authentication.repository;


import io.github.darlene.surveyplatformbackend.authentication.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long>{

    Optional<RefreshToken> findByToken(String token);

}