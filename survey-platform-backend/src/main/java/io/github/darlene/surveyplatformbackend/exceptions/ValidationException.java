package io.github.darlene.surveyplatformbackend.exceptions;



public class ValidationException extends RuntimeException{
    public ValidationException(String message){
        super(message);
    }
}