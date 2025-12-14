package com.redcare.git.demo.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitHubFeignConfig {

    @Value("${github.api.token}")
    private String githubToken;

    @Value("${github.api.template}")
    private String template;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + githubToken);
            requestTemplate.header("Accept", "application/vnd.github+json");
            requestTemplate.header("X-GitHub-Api-Version", template);
        };
    }
}
