package io.github.darlene.surveyplatformbackend.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum QuestionType {
    SHORT_TEXT("short_text"),
    LONG_TEXT("long_text"),
    EMAIL("email"),
    CHOICE("choice"),
    FILE("file");

    private final String wireValue;

    QuestionType(String wireValue) {
        this.wireValue = wireValue;
    }

    public static QuestionType fromWireValue(String value) {
        return Arrays.stream(values())
                .filter(t -> t.wireValue.equals(value)) // Keeps only stream elements where condition is true.
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown question type: " + value));
    }
}