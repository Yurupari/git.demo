package com.redcare.git.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ParameterEnum {
    LANGUAGE("language"),
    CREATED_AT("created_at"),
    UPDATED_AT("updated_at"),
    STARGAZERS_COUNT("stargazers_count"),
    FORKS_COUNT("forks_count"),
    POPULARITY_SCORE("popularity_score");

    private final String value;

    public static ParameterEnum fromValue(String value) {
        return Arrays.stream(ParameterEnum.values())
                .filter(enumValue -> enumValue.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown query parameter: " + value));
    }
}
