package io.github.darlene.surveyplatformbackend.exceptions;

public class InvalidCredentialsException extends RuntimeException{

    public InvalidCredentialsException(String message){
        super(message);
    }
}
