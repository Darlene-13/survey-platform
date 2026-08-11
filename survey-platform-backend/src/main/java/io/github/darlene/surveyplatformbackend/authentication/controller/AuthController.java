package io.github.darlene.surveyplatformbackend.authentication.controller;


import io.github.darlene.surveyplatformbackend.authentication.dto.LoginRequestXml;
import io.github.darlene.surveyplatformbackend.authentication.dto.LoginResponseXml;
import io.github.darlene.surveyplatformbackend.authentication.dto.RefreshTokenXml;
import io.github.darlene.surveyplatformbackend.authentication.dto.RegisterRequestXml;
import io.github.darlene.surveyplatformbackend.authentication.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(value = "/api/v1/auth", produces = APPLICATION_XML_VALUE)
@RequiredArgsConstructor
public class AuthController{

    private final AuthService authService;

    // Method to register a new user
    @PostMapping(value = "/register", consumes = APPLICATION_XML_VALUE)
    public LoginResponseXml register(@Valid @RequestBody RegisterRequestXml request){
        return authService.register(request);
    }

    // Method to login
    @PostMapping(value = "/login", consumes = APPLICATION_XML_VALUE)
    public LoginResponseXml login(@Valid @RequestBody LoginRequestXml request){
        return authService.response(request);
    }

    // Method to issue a new access token
    @PostMapping(value = "/refresh", consumes = APPLICATION_XML_VALUE)
    public LoginResponseXml refresh(@Valid @RequestBody RefreshTokenXml request){
        return authService.refreshToken(request.getRefreshToken());
    }

    // Method to revoke a refresh token
    @PostMapping(value = "/logout", consumes = APPLICATION_XML_VALUE)
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenXml request){
        authService.logout(request.getRefreshToken());
        return ResponseEntity.noContent().build();
    }
}
