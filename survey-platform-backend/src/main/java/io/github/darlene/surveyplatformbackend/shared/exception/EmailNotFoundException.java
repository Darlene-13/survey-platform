package io.github.darlene.surveyplatformbackend.shared.exception;

public class EmailNotFoundException extends RuntimeException {

    public EmailNotFoundException(String email) {
        super("User not found with email: " + email);
    }
}
