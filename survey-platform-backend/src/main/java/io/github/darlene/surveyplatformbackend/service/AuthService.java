package io.github.darlene.surveyplatformbackend.service;


import io.github.darlene.surveyplatformbackend.api.dto.RegisterRequestXml;
import io.github.darlene.surveyplatformbackend.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.darlene.surveyplatformbackend.exceptions.DuplicateEmailException;
import io.github.darlene.surveyplatformbackend.exceptions.InvalidCredentialsException;

import io.github.darlene.surveyplatformbackend.repository.UserRepository;
import io.github.darlene.surveyplatformbackend.api.dto.LoginResponseXml;
import io.github.darlene.surveyplatformbackend.api.dto.RefreshTokenXml;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // Register user
    public LoginResponseXml register(RegisterRequestXml registerRequestXml){

    }

    // Login User
    public LoginResponseXml login(LoginResponseXml loginResponseXml){

    }

    //refreshToken
    public RefreshTokenXml refreshToken(String token){

    }


    //logout
    public void logout(String refreshToken){

    }


    // generateRefreshToken
    private RefreshToken generateRefreshToken(){

    }

}
