package com.redcare.git.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

public record GitPopularityResponse(
        @Schema(description = "Total number of repositories found", example = "1000")
        Integer totalCount,
        @Schema(description = "Indicates if the query timed out", example = "false")
        Boolean incompleteResults,
        @Schema(description = "List of repositories found", example = "[{..., popularity_score: 100}]")
        List<Map<String, Object>> items
) {
}
