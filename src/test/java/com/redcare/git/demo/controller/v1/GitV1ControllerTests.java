package com.redcare.git.demo.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redcare.git.demo.dto.GitPopularityRequest;
import com.redcare.git.demo.dto.GitPopularityResponse;
import com.redcare.git.demo.enums.QueryParameterEnum;
import com.redcare.git.demo.service.GitHubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class GitV1ControllerTests {

    @Autowired
    private GitV1Controller gitV1Controller;

    @MockBean
    private GitHubService gitHubService;

    @BeforeEach
    void setUp() throws IOException {
        var objectMapper = new ObjectMapper();
        var resource = new ClassPathResource("response/git-popularity-response.json");
        var searchResponse = objectMapper.readValue(resource.getInputStream(), GitPopularityResponse.class);
        when(gitHubService.calculatePopularityScore(any())).thenReturn(searchResponse);
    }

    @Test
    void testCalculatePopularityScore() {
        var request = new GitPopularityRequest(
                Map.of(
                        QueryParameterEnum.LANGUAGE, "java",
                        QueryParameterEnum.CREATED, "2025-11-28"
                ),
                10,
                1
        );

        var response = gitV1Controller.calculatePopularityScore(request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getItems());
        assertEquals(5, response.getBody().getItems().size());
        response.getBody().getItems().forEach(item -> {
            assertTrue(item.has("popularity_score"));
            assertNotNull(item.get("popularity_score"));
        });
    }
}
