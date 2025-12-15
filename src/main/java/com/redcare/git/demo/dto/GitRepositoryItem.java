package com.redcare.git.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

@Schema(description = "Details of a GitHub repository with its calculated popularity score")
public record GitRepositoryItem(
        Long id,
        String name,
        @JsonProperty("full_name")
        String fullName,
        @Schema(description = "Owner of the repository")
        Owner owner,
        @JsonProperty("html_url")
        String htmlUrl,
        String description,
        @JsonProperty("stargazers_count")
        Integer stargazersCount,
        @JsonProperty("forks_count")
        Integer forksCount,
        @JsonProperty("updated_at")
        Instant updatedAt,
        @JsonProperty("popularity_score")
        BigDecimal popularityScore
) {
    @Schema(description = "Repository owner details")
    public record Owner(
            String login,
            Long id,
            @JsonProperty("avatar_url")
            String avatarUrl,
            @JsonProperty("html_url")
            String htmlUrl
    ) {}
}
