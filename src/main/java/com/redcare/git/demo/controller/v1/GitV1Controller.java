package com.redcare.git.demo.controller.v1;

import com.redcare.git.demo.dto.GitPopularityRequest;
import com.redcare.git.demo.dto.GitPopularityResponse;
import com.redcare.git.demo.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/git/v1")
public class GitV1Controller {
    @Autowired
    private GitHubService gitHubService;

    @PostMapping("/popularity/score")
    public ResponseEntity<GitPopularityResponse> calculatePopularityScore(@RequestBody GitPopularityRequest gitPopularityRequest) {
        return ResponseEntity.ok(gitHubService.calculatePopularityScore(gitPopularityRequest));
    }
}
