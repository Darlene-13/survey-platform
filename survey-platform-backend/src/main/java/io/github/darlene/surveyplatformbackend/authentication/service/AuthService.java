package io.github.darlene.surveyplatformbackend.authentication.service;


import io.github.darlene.surveyplatformbackend.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService{

    private final UserRepository userRepository;

    //Method to login



}
