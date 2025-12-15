package com.redcare.git.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redcare.git.demo.dto.GitPopularityRequest;
import com.redcare.git.demo.dto.GitRepositoriesResponse;
import com.redcare.git.demo.exception.GitHubServiceException;
import com.redcare.git.demo.feign.GitHubFeignClient;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private GitHubFeignClient gitHubFeignClient;

	@Test
	void calculatePopularityScore_Success() throws Exception {
		var resource = new ClassPathResource("response/git-repositories-response.json");
		var searchResponse = objectMapper.readValue(resource.getInputStream(), GitRepositoriesResponse.class);

		resource = new ClassPathResource("request/git-popularity-request.json");
		var requestJson = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

		when(gitHubFeignClient.searchRepositories(any(), any(), any(), any(), any())).thenReturn(searchResponse);

		mockMvc.perform(post("/api/git/v1/popularity/score")
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestJson)
				)
				.andExpect(status().isOk());
	}

	@Test
	void calculatePopularityScore_ServiceUnavailable() throws Exception {
		var resource = new ClassPathResource("request/git-popularity-request.json");
		var requestJson = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

		when(gitHubFeignClient.searchRepositories(any(), any(), any(), any(), any())).thenThrow(new GitHubServiceException("GitHub API is unavailable"));

		mockMvc.perform(post("/api/git/v1/popularity/score")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson)
				)
				.andExpect(status().is5xxServerError());
	}

	@Test
	void calculatePopularityScore_BadGateway() throws Exception {
		var resource = new ClassPathResource("response/git-repositories-response-date-corrupted-format.json");
		var searchResponse = objectMapper.readValue(resource.getInputStream(), GitRepositoriesResponse.class);

		resource = new ClassPathResource("request/git-popularity-request.json");
		var requestJson = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

		when(gitHubFeignClient.searchRepositories(any(), any(), any(), any(), any())).thenReturn(searchResponse);

		mockMvc.perform(post("/api/git/v1/popularity/score")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson)
				)
				.andExpect(status().is5xxServerError());
	}

	@Test
	void calculatePopularityScore_BadRequest() throws Exception {
		var resource = new ClassPathResource("request/git-popularity-bad-request.json");
		var requestJson = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

		mockMvc.perform(post("/api/git/v1/popularity/score")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson)
				)
				.andExpect(status().is4xxClientError());
	}

	@Test
	void calculatePopularityScore_InternalServerError() throws Exception {
		var resource = new ClassPathResource("request/git-popularity-request.json");
		var requestJson = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

		when(gitHubFeignClient.searchRepositories(any(), any(), any(), any(), any())).thenThrow(new RuntimeException("Unexpected error"));

		mockMvc.perform(post("/api/git/v1/popularity/score")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson)
				)
				.andExpect(status().is5xxServerError());
	}
}
