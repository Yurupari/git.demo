package com.redcare.git.demo.dto;

import com.redcare.git.demo.enums.ParameterEnum;

import java.util.Map;

public record GitPopularityRequest(
        Map<ParameterEnum, String> parameters,
        Integer perPage,
        Integer page
) {
}
