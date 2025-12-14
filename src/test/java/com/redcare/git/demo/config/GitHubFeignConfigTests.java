package com.redcare.git.demo.config;

import feign.RequestInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class GitHubFeignConfigTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void requestInterceptor_isCreatedAndLoaded() {
        RequestInterceptor interceptor = context.getBean(RequestInterceptor.class);
        assertNotNull(interceptor, "The RequestInterceptor bean should be created and loaded into the context.");
    }
}
