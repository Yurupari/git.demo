package com.redcare.git.demo.controller;

import com.redcare.git.demo.dto.GitSearchRequest;
import com.redcare.git.demo.dto.GitSearchResponse;
import com.redcare.git.demo.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/git/v1")
public class GitController {
    @Autowired
    private GitHubService gitHubService;

    @GetMapping("/search/repositories")
    public ResponseEntity<GitSearchResponse> searchRepositories(
            @RequestParam("query") String query,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "perPage", required = false) Integer perPage,
            @RequestParam(name = "page", required = false) Integer page
    ) {
        return ResponseEntity.ok(gitHubService.searchRepositories(new GitSearchRequest(query, sort, order, perPage, page)));
    }
}
