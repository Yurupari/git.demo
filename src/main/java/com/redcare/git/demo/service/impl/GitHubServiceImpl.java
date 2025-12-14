package com.redcare.git.demo.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redcare.git.demo.dto.GitPopularityRequest;
import com.redcare.git.demo.dto.GitPopularityResponse;
import com.redcare.git.demo.enums.QueryParameterEnum;
import com.redcare.git.demo.exception.GitDataParseException;
import com.redcare.git.demo.feign.GitHubFeignClient;
import com.redcare.git.demo.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GitHubServiceImpl implements GitHubService {

    private final GitHubFeignClient gitHubFeignClient;

    @Value("${github.weights.stars}")
    private double STARS_WEIGHT;

    @Value("${github.weights.forks}")
    private double FORKS_WEIGHT;

    @Value("${github.weights.days_since_update}")
    private double DAYS_SINCE_UPDATE_WEIGHT;

    @Override
    public GitPopularityResponse calculatePopularityScore(GitPopularityRequest gitPopularityRequest) {
        var gitSearchResponse = gitHubFeignClient.searchRepositories(
                formatQuery(gitPopularityRequest.parameters()),
                null,
                null,
                gitPopularityRequest.perPage(),
                gitPopularityRequest.page()
        );

        return Optional.ofNullable(gitSearchResponse)
                .map(response -> {
                    var itemsWithScore = response.items().stream()
                            .map(item -> {
                                var objectNode = (ObjectNode) item;
                                objectNode.put("popularity_score", calculatePopularity(item));
                                return objectNode;
                            })
                            .toList();

                    return GitPopularityResponse.builder()
                            .totalCount(response.totalCount())
                            .incompleteResults(response.incompleteResults())
                            .items(itemsWithScore)
                            .build();
                })
                .orElse(null);
    }

    private String formatQuery(Map<QueryParameterEnum, String> queryParameters) {
        return queryParameters.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isBlank())
                .map(entry -> {
                    var parameter = entry.getKey();
                    var parameterValue = entry.getValue();

                    return switch (parameter) {
                        case CREATED, UPDATED -> String.format("%s:>%s", parameter.getValue(), parameterValue);
                        default -> String.format("%s:%s", parameter.getValue(), parameterValue);
                    };
                })
                .collect(Collectors.joining(" "));
    }

    private BigDecimal calculatePopularity(JsonNode item) {
        var stars = item.has("stargazers_count") ? item.get("stargazers_count").asInt() : 0;
        var forks = item.has("forks_count") ? item.get("forks_count").asInt() : 0;

        long daysSinceUpdate = 0;
        if (item.has("updated_at") && !item.get("updated_at").isNull()) {
            try {
                Instant updatedAt = Instant.parse(item.get("updated_at").asText());
                daysSinceUpdate = Duration.between(updatedAt, Instant.now()).toDays();
            } catch (DateTimeParseException e) {
                throw new GitDataParseException("Failed to parse date from GitHub API");
            }
        }

        return BigDecimal.valueOf(stars * STARS_WEIGHT + forks * FORKS_WEIGHT - daysSinceUpdate * DAYS_SINCE_UPDATE_WEIGHT);
    }
}
