package io.github.darlene.surveyplatformbackend.shared.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message, Long id){
        super(message);
    }
}
