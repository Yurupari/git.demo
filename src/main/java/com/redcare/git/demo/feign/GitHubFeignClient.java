package com.redcare.git.demo.feign;

import com.redcare.git.demo.config.GitHubFeignConfig;
import com.redcare.git.demo.dto.GitRepositoriesResponse;
import com.redcare.git.demo.feign.fallback.GitHubFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "github",
        url = "${github.api.url}",
        configuration = GitHubFeignConfig.class,
        fallback = GitHubFeignClientFallBack.class
)
public interface GitHubFeignClient {

    @GetMapping("/search/repositories")
    GitRepositoriesResponse searchRepositories(
            @RequestParam("q") String q,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "per_page", required = false) Integer perPage,
            @RequestParam(name = "page", required = false) Integer page
    );
}
