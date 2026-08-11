package io.github.darlene.surveyplatformbackend;

import io.github.darlene.surveyplatformbackend.authentication.dto.LoginRequestXml;
import io.github.darlene.surveyplatformbackend.authentication.dto.LoginResponseXml;
import io.github.darlene.surveyplatformbackend.authentication.dto.RegisterRequestXml;
import io.github.darlene.surveyplatformbackend.authentication.model.UserRole;
import io.github.darlene.surveyplatformbackend.authentication.service.AuthService;
import io.github.darlene.surveyplatformbackend.shared.exception.InvalidTokenException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthenticationFlowTests {

    @Autowired AuthService authService;

    @Test
    void respondentCanRegisterLoginRefreshAndLogout() {
        RegisterRequestXml registration = new RegisterRequestXml();
        registration.setFirstName("Test");
        registration.setLastName("Respondent");
        registration.setEmail("flow@example.com");
        registration.setPassword("Password123!");

        LoginResponseXml registered = authService.register(registration);
        assertEquals(UserRole.RESPONDENT, registered.role());
        assertNotNull(registered.user());
        assertNotNull(registered.token());

        LoginRequestXml login = new LoginRequestXml();
        login.setEmail(registration.getEmail());
        login.setPassword(registration.getPassword());
        LoginResponseXml authenticated = authService.response(login);
        assertEquals(registration.getEmail(), authenticated.user().email());

        LoginResponseXml refreshed = authService.refreshToken(authenticated.refreshToken());
        assertNotNull(refreshed.token());

        authService.logout(authenticated.refreshToken());
        assertThrows(InvalidTokenException.class, () -> authService.refreshToken(authenticated.refreshToken()));
    }
}
