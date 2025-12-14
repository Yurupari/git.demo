package com.redcare.git.demo.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redcare.git.demo.config.GitHubProperties;
import com.redcare.git.demo.dto.GitPopularityRequest;
import com.redcare.git.demo.dto.GitPopularityResponse;
import com.redcare.git.demo.enums.ParameterEnum;
import com.redcare.git.demo.exception.GitDataParseException;
import com.redcare.git.demo.feign.GitHubFeignClient;
import com.redcare.git.demo.service.GitHubService;
import lombok.RequiredArgsConstructor;
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
    private final GitHubProperties gitHubProperties;

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

    private String formatQuery(Map<ParameterEnum, String> queryParameters) {
        return queryParameters.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isBlank())
                .map(entry -> {
                    var parameter = entry.getKey();
                    var parameterValue = entry.getValue();

                    return switch (parameter) {
                        case CREATED_AT, UPDATED_AT -> String.format("%s:>%s", parameter.getValue(), parameterValue);
                        default -> String.format("%s:%s", parameter.getValue(), parameterValue);
                    };
                })
                .collect(Collectors.joining(" "));
    }

    private BigDecimal calculatePopularity(JsonNode item) {
        final double[] popularityScore = {0.0};

        gitHubProperties.getWeights().forEach((key, value) -> {
            if (item.has(key)) {
                switch (ParameterEnum.fromValue(key)) {
                    case CREATED_AT, UPDATED_AT -> {
                        try {
                            Instant instant = Instant.parse(item.get(key).asText());
                            var daysSince = Duration.between(instant, Instant.now()).toDays();
                            popularityScore[0] += daysSince * value;
                        } catch (DateTimeParseException e) {
                            throw new GitDataParseException("Failed to parse date from GitHub API");
                        }
                    }
                    default -> popularityScore[0] += item.get(key).asInt() * value;
                }
            }
        });

        return BigDecimal.valueOf(popularityScore[0]);
    }
}
