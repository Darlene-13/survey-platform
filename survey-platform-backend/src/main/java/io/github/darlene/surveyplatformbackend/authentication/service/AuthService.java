package io.github.darlene.surveyplatformbackend.authentication.service;


import io.github.darlene.surveyplatformbackend.authentication.dto.LoginRequestXml;
import io.github.darlene.surveyplatformbackend.authentication.dto.LoginResponseXml;
import io.github.darlene.surveyplatformbackend.authentication.dto.RegisterRequestXml;
import io.github.darlene.surveyplatformbackend.authentication.dto.UserXml;
import io.github.darlene.surveyplatformbackend.authentication.model.RefreshToken;
import io.github.darlene.surveyplatformbackend.authentication.model.User;
import io.github.darlene.surveyplatformbackend.authentication.model.UserRole;
import io.github.darlene.surveyplatformbackend.authentication.repository.UserRepository;
import io.github.darlene.surveyplatformbackend.authentication.repository.RefreshTokenRepository;
import io.github.darlene.surveyplatformbackend.shared.exception.EmailNotFoundException;
import io.github.darlene.surveyplatformbackend.shared.exception.DuplicateEmailException;
import io.github.darlene.surveyplatformbackend.shared.exception.InvalidCredentialsException;
import io.github.darlene.surveyplatformbackend.shared.exception.InvalidTokenException;
import io.github.darlene.surveyplatformbackend.shared.exception.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService{

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    // method to register
    public LoginResponseXml register(RegisterRequestXml request){
        userRepository.findByEmailIgnoreCase(request.getEmail())
                .ifPresent(u -> {throw new DuplicateEmailException("Email is already registered");
                });
        //Create the user
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail().trim().toLowerCase())
                .hashedPassword(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.RESPONDENT)
                .build();

        userRepository.save(user);

        String accessToken = jwtTokenProvider.generateToken(user.getEmail(), user.getRole());
        RefreshToken refreshToken = generateRefreshToken(user);
        return loginResponse(user, accessToken, refreshToken);

    }

    //Method to login
    @Transactional
    public LoginResponseXml response(LoginRequestXml request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new EmailNotFoundException(request.getEmail()));

        String accessToken = jwtTokenProvider.generateToken(user.getEmail(), user.getRole());
        RefreshToken refreshToken = generateRefreshToken(user);

        return loginResponse(user, accessToken, refreshToken);
    }

    public LoginResponseXml refreshToken(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Refresh Token Expired!"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())){
            refreshTokenRepository.delete(refreshToken);
            throw new TokenExpiredException("Refresh Token Expired");
        }

        User user = refreshToken.getUser();
        String newAccessToken = jwtTokenProvider.generateToken(
                user.getEmail(),
                user.getRole()
        );

        return loginResponse(user, newAccessToken, refreshToken);

    }

    public void logout(String refreshToken){
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);
    }

    // method to log out
    private RefreshToken generateRefreshToken(User user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        refreshToken.setUser(user);
        return refreshTokenRepository.save(refreshToken);
    }

    private LoginResponseXml loginResponse(User user, String accessToken, RefreshToken refreshToken){
        UserXml userXml = new UserXml(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole().name());
        return LoginResponseXml.builder()
                .token(accessToken)
                .user(userXml)
                .type("Bearer")
                .expiresIn(LocalDateTime.now().plusHours(24))
                .email(user.getEmail())
                .role(user.getRole())
                .refreshToken(refreshToken.getToken())
                .build();
    }
}
