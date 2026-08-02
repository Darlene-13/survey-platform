package io.github.darlene.surveyplatformbackend.shared.exception;



public class ValidationException extends RuntimeException{
    public ValidationException(String message){
        super(message);
    }
}