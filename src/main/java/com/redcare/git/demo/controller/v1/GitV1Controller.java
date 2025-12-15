package com.redcare.git.demo.controller.v1;

import com.redcare.git.demo.dto.GitPopularityRequest;
import com.redcare.git.demo.dto.GitPopularityResponse;
import com.redcare.git.demo.service.GitHubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/git/v1")
public class GitV1Controller {
    @Autowired
    private GitHubService gitHubService;

    @Operation(summary = "Calculate repository popularity", description = "Calculates a popularity score for Git repositories based on filters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated the score"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/popularity/score")
    public ResponseEntity<GitPopularityResponse> calculatePopularityScore(@RequestBody GitPopularityRequest gitPopularityRequest) {
        return ResponseEntity.ok(gitHubService.calculatePopularityScore(gitPopularityRequest));
    }
}
