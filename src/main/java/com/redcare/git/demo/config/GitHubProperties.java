package com.redcare.git.demo.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Getter
@ConfigurationProperties(prefix = "github")
public class GitHubProperties {
    private final Map<String, String> api;
    private final Map<String, Double> weights;

    @ConstructorBinding
    public GitHubProperties(Map<String, String> api, Map<String, Double> weights) {
        this.api = api;
        this.weights = weights;
    }
}
