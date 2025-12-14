package com.redcare.git.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum QueryParameterEnum {
    LANGUAGE("language"),
    CREATED("created"),
    UPDATED("updated");

    private final String value;

    public static QueryParameterEnum fromValue(String value) {
        return Arrays.stream(QueryParameterEnum.values())
                .filter(enumValue -> enumValue.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown query parameter: " + value));
    }
}
