package com.redcare.git.demo.error;

import com.redcare.git.demo.exception.GitDataParseException;
import com.redcare.git.demo.exception.GitHubServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleGitHubServiceException(GitHubServiceException e) {
        log.error("The GitHub service is currently unavailable", e);
        return ResponseEntity.status(500).body("The GitHub service is currently unavailable. Please try again later.");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleGitDataParseException(GitDataParseException e) {
        log.error("An error occurred while parsing data", e);
        return ResponseEntity.status(500).body("An error occurred while parsing data from GitHub API.");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Unknown query parameter", e);
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e) {
        log.error("An error occurred", e);
        return ResponseEntity.status(500).body("Something went wrong");
    }
}
