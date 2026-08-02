package io.github.darlene.surveyplatformbackend.shared.exception;

public class ResourceInUse extends RuntimeException{
    public ResourceInUse(String message){
        super(message);
    }
}
