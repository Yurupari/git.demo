package com.redcare.git.demo.dto;

import com.redcare.git.demo.enums.ParameterEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

public record GitPopularityRequest(
        @Schema(description = "Filters for the GitHub API query", example = "{'language': 'java', 'created_at': '2023-01-01'}")
        Map<ParameterEnum, String> parameters,
        @Schema(description = "Number of results per page", example = "10")
        Integer perPage,
        @Schema(description = "Page number of the results", example = "1")
        Integer page
) {
}
