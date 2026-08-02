package io.github.darlene.surveyplatformbackend.authentication.model;

import lombok.*;

@Getter @NoArgsConstructor
public enum UserRole{

    ADMIN("ADMIN"),
    RESPONDENT("RESPONDENT");

    private String description;

    UserRole(String description){
        this.description = description;
    }
}