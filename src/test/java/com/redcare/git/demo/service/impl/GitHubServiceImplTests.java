package com.redcare.git.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redcare.git.demo.dto.GitPopularityRequest;
import com.redcare.git.demo.dto.GitRepositoriesResponse;
import com.redcare.git.demo.enums.ParameterEnum;
import com.redcare.git.demo.exception.GitDataParseException;
import com.redcare.git.demo.feign.GitHubFeignClient;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class GitHubServiceImplTests {
    @Autowired
    private GitHubServiceImpl gitHubServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GitHubFeignClient gitHubFeignClient;

    @Test
    void testGetPopularityScore_CalculatesScoreCorrectly() throws IOException {
        var resource = new ClassPathResource("response/git-repositories-response.json");
        var gitResponse = objectMapper.readValue(resource.getInputStream(), GitRepositoriesResponse.class);
        when(gitHubFeignClient.searchRepositories(any(), any(), any(), any(), any())).thenReturn(gitResponse);

        var request = new GitPopularityRequest(
                Map.of(
                        ParameterEnum.LANGUAGE, "java",
                        ParameterEnum.CREATED_AT, "2025-11-28"
                ),
                5,
                1
        );

        var fixedNow = Instant.parse("2025-12-14T12:00:00Z");

        try (MockedStatic<Instant> mockedStatic = Mockito.mockStatic(Instant.class, Mockito.CALLS_REAL_METHODS)) {
            mockedStatic.when(Instant::now).thenReturn(fixedNow);

            var response = gitHubServiceImpl.calculatePopularityScore(request);

            assertNotNull(response);
            assertNotNull(response.items());
            assertEquals(5, response.items().size());

            var popularityScores = Arrays.asList(493.2, 192.0, 183.0, 172.0, 172.0);
            response.items().forEach(item -> {
                assertTrue(item.containsKey(ParameterEnum.POPULARITY_SCORE.getValue()));
                var popularityScore = item.get(ParameterEnum.POPULARITY_SCORE.getValue());
                assertNotNull(popularityScore);
                assertTrue(popularityScores.contains(((BigDecimal) popularityScore).doubleValue()));
            });
        }
    }

    @Test
    void testGetPopularityScore_ErrorDataParsing() throws IOException {
        var resource = new ClassPathResource("response/git-repositories-response-date-corrupted-format.json");
        var gitResponse = objectMapper.readValue(resource.getInputStream(), GitRepositoriesResponse.class);
        when(gitHubFeignClient.searchRepositories(any(), any(), any(), any(), any())).thenReturn(gitResponse);

        var request = new GitPopularityRequest(
                Map.of(
                        ParameterEnum.LANGUAGE, "java",
                        ParameterEnum.CREATED_AT, "2025-11-28"
                ),
                5,
                1
        );

        assertThrows(GitDataParseException.class, () -> gitHubServiceImpl.calculatePopularityScore(request));
    }
}
