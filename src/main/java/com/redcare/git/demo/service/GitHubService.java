package com.redcare.git.demo.service;

import com.redcare.git.demo.dto.GitSearchRequest;
import com.redcare.git.demo.dto.GitSearchResponse;
import com.redcare.git.demo.feign.GitHubFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitHubService {

    private final GitHubFeignClient gitHubFeignClient;

    public GitSearchResponse searchRepositories(GitSearchRequest gitSearchRequest) {
        return gitHubFeignClient.searchRepositories(
                gitSearchRequest.query(),
                gitSearchRequest.sort(),
                gitSearchRequest.order(),
                gitSearchRequest.perPage(),
                gitSearchRequest.page()
        );
    }
}
