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
import com.redcare.git.demo.util.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    @Cacheable("popularity-scores")
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
                                var mapNode = Utility.convertJsonNodeToMap(item);
                                mapNode.put(ParameterEnum.POPULARITY_SCORE.getValue(), calculatePopularity(item));
                                return mapNode;
                            })
                            .toList();

                    return new GitPopularityResponse(
                            response.totalCount(),
                            response.incompleteResults(),
                            itemsWithScore
                    );
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
                        case CREATED_AT, UPDATED_AT -> String.format("%s:>%s", parameter.getValue().replace("_at", ""), parameterValue);
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
                            if (daysSince >= 0) {
                                popularityScore[0] += value / (daysSince + 1.0);
                            }
                        } catch (DateTimeParseException e) {
                            throw new GitDataParseException("Failed to parse date from GitHub API");
                        }
                    }
                    default -> popularityScore[0] += item.get(key).asDouble() * value;
                }
            }
        });

        return BigDecimal.valueOf(popularityScore[0]).setScale(1, RoundingMode.HALF_UP);
    }
}
