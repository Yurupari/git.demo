package com.redcare.git.demo.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class GitHubPropertiesTests {

    @Autowired
    private GitHubProperties gitHubProperties;

    @Test
    void testWeightsAreLoaded() {
        assertNotNull(gitHubProperties);
        assertNotNull(gitHubProperties.getApi());
        assertNotNull(gitHubProperties.getWeights());
        assertFalse(gitHubProperties.getApi().isEmpty());
        assertFalse(gitHubProperties.getWeights().isEmpty());
    }
}
