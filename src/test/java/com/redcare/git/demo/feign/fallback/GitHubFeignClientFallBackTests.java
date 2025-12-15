package com.redcare.git.demo.feign.fallback;

import com.redcare.git.demo.exception.GitHubServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class GitHubFeignClientFallBackTests {

    @Autowired
    private GitHubFeignClientFallBack gitHubFeignClientFallBack;

    @Test
    void testSearchRepositories() {
        assertThrows(GitHubServiceException.class, () -> gitHubFeignClientFallBack.searchRepositories(null, null, null, null, null));
    }
}