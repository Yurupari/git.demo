package com.redcare.git.demo.feign.fallback;

import com.redcare.git.demo.dto.GitSearchResponse;
import com.redcare.git.demo.feign.GitHubFeignClient;
import org.springframework.stereotype.Component;

@Component
public class GitHubFeignClientFallBack implements GitHubFeignClient {
    @Override
    public GitSearchResponse searchRepositories(String q, String sort, String order, Integer perPage, Integer page) {
        return null;
    }
}
