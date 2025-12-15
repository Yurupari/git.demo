package com.redcare.git.demo.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitHubFeignConfig {

    @Autowired
    private GitHubProperties gitHubProperties;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + gitHubProperties.getApi().get("token"));
            requestTemplate.header("Accept", "application/vnd.github+json");
            requestTemplate.header("X-GitHub-Api-Version", gitHubProperties.getApi().get("template"));
        };
    }
}
