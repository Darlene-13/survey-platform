package io.github.darlene.surveyplatformbackend.exceptions;

public class ResourceInUse extends RuntimeException{
    public ResourceInUse(String message){
        super(message);
    }
}
