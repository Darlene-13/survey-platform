package io.github.darlene.surveyplatformbackend.domain;

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