package io.github.darlene.surveyplatformbackend.authentication.controller;


import io.github.darlene.surveyplatformbackend.authentication.dto.RegisterRequestXml;
import io.github.darlene.surveyplatformbackend.authentication.service.AuthService;
import io.github.darlene.surveyplatformbackend.authentication.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthController{

    private final AuthService AuthService;
    private final RefreshTokenService refreshTokenService;

    // Method to register a new user
    @PostMapping
    @RequestMapping
    public RegisterRequestXml register(@Valid @RequestBody RegisterRequestXml register){

    }


}