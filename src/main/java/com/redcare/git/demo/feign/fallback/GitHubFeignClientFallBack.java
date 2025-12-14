package com.redcare.git.demo.feign.fallback;

import com.redcare.git.demo.dto.GitRepositoriesResponse;
import com.redcare.git.demo.exception.GitHubServiceException;
import com.redcare.git.demo.feign.GitHubFeignClient;
import org.springframework.stereotype.Component;

@Component
public class GitHubFeignClientFallBack implements GitHubFeignClient {
    @Override
    public GitRepositoriesResponse searchRepositories(String q, String sort, String order, Integer perPage, Integer page) {
        throw new GitHubServiceException("Failed to fetch data from GitHub API. The service may be temporarily unavailable.");
    }
}
