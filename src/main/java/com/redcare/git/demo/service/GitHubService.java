package com.redcare.git.demo.service;

import com.redcare.git.demo.dto.GitPopularityRequest;
import com.redcare.git.demo.dto.GitPopularityResponse;

public interface GitHubService {
    public GitPopularityResponse calculatePopularityScore(GitPopularityRequest gitPopularityRequest);
}
