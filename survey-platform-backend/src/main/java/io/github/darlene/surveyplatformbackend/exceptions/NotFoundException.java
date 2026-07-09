package io.github.darlene.surveyplatformbackend.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message, Long id){
        super(message);
    }
}
