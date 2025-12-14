package com.redcare.git.demo.service;

import com.redcare.git.demo.dto.GitPopularityRequest;
import com.redcare.git.demo.dto.GitPopularityResponse;
import com.redcare.git.demo.dto.GitSearchRequest;
import com.redcare.git.demo.dto.GitSearchResponse;

public interface GitHubService {

    public GitSearchResponse searchRepositories(GitSearchRequest gitSearchRequest);
    public GitPopularityResponse getPopularityScore(GitPopularityRequest gitPopularityRequest);
}
