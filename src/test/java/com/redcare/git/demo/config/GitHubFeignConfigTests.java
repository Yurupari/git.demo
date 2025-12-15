package com.redcare.git.demo.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class GitHubFeignConfigTests {

    @Autowired
    private GitHubFeignConfig gitHubFeignConfig;

    @Test
    void requestInterceptor_isCreatedAndLoaded() {
        var interceptor = gitHubFeignConfig.requestInterceptor();
        assertNotNull(interceptor);
    }
}