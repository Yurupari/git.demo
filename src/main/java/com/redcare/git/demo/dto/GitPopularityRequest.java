package com.redcare.git.demo.dto;

import com.redcare.git.demo.enums.QueryParameterEnum;

import java.util.Map;

public record GitPopularityRequest(
        Map<QueryParameterEnum, String> parameters,
        Integer perPage,
        Integer page
) {
}
